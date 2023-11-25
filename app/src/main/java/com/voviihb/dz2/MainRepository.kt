package com.voviihb.dz2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository constructor(private val apiService: ApiService) {

    suspend fun loadDogImage() =
        withContext(Dispatchers.IO) {
            apiService.loadDogImage()
        }

    suspend fun loadDogImages() = withContext(Dispatchers.IO) {
        apiService.loadDogImages()
    }

}