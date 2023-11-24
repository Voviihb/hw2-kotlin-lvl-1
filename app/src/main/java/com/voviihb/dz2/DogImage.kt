package com.voviihb.dz2

import com.google.gson.annotations.SerializedName

/**
 * POJO class for DogImage
 * @param message contains URL for dog image
 * @param status contains success status
 */
data class DogImage(
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: String
)

/**
 * POJO class for DogImageList
 * @param message contains List of URLs for dog images
 * @param status contains success status
 */
data class DogImageList(
    @SerializedName("message") var message: List<String>,
    @SerializedName("status") var status: String
)
