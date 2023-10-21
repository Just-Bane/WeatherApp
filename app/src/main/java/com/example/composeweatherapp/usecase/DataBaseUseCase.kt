package com.example.composeweatherapp.usecase

import com.example.composeweatherapp.repository.database.DBRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataBaseUseCase @Inject constructor(
    private val db: DBRepo
) {
    suspend fun getDB() = withContext(Dispatchers.IO) {
        return@withContext try {
            db.getDB()
        } catch (e: DBRepo.DoNotInitializeException) {
            initDataBase()
            db.getDB()
        }
    }

    private suspend fun initDataBase() {
        db.init()
    }
}