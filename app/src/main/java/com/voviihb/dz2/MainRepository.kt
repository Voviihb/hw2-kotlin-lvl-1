package com.voviihb.dz2

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository constructor(private val apiService: ApiService) {

    suspend fun loadDogImage(exceptionHandler: CoroutineExceptionHandler) =
        withContext(Dispatchers.IO + exceptionHandler) {
            apiService.loadDogImage()
        }

}