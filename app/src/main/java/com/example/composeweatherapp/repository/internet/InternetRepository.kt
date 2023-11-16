package com.example.composeweatherapp.repository.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InternetRepository @Inject constructor(
    private val context: Context
) : ConnectivityObserver, CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    var networkStatusObserver = mutableStateOf(ConnectivityObserver.Status.Lost)

    init {
        launch {
            observe().collect {
                networkStatusObserver.value = it
                Log.i("network", "getNetworkStatus " + it.name)
            }
        }
    }

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        if (isInternetAvailable()) {
                            send(ConnectivityObserver.Status.Available)
                            notifyInetStateChanged(ConnectivityObserver.Status.Available)
                        }
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(ConnectivityObserver.Status.Losing)
                        notifyInetStateChanged(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(ConnectivityObserver.Status.Lost)
                        notifyInetStateChanged(ConnectivityObserver.Status.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(ConnectivityObserver.Status.Unavailable)
                        notifyInetStateChanged(ConnectivityObserver.Status.Unavailable)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val address = InetSocketAddress("8.8.8.8", 53)
            val socket = java.net.Socket()
            socket.connect(address, 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    private var buffState: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable
    private var mSubscriptions = ArrayList<ISubscription>()

    fun subscribeOnInetState(subscription: ISubscription) {
        mSubscriptions.add(subscription)
        subscription.onStatusChanged(buffState)
    }

    fun unsubscribeInetState(subscription: ISubscription) {
        mSubscriptions.remove(subscription)
    }

    private fun notifyInetStateChanged(state: ConnectivityObserver.Status) {
        buffState = state
        mSubscriptions.forEach { subscription ->
            subscription.onStatusChanged(state)
        }
    }

    interface ISubscription {
        fun onStatusChanged(status: ConnectivityObserver.Status)
    }
}