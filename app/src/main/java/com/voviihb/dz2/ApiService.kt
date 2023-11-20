package com.voviihb.dz2

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val COUNT = 10

interface ApiService {
    @GET("image/random")
    suspend fun loadDogImage(): Response<DogImage>
    @GET("image/random/{count}")
    suspend fun loadDogImages(@Path("count") count: Int = COUNT): Response<List<DogImage>>
}