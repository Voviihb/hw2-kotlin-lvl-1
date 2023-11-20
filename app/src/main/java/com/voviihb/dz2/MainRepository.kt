package com.voviihb.dz2

class MainRepository constructor(private val apiService: ApiService) {

    suspend fun loadDogImage() = apiService.loadDogImage()

}