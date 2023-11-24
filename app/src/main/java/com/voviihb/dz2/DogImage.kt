package com.voviihb.dz2

import com.google.gson.annotations.SerializedName

data class DogImage(
    /**
     * contains URL for dog image
     */
    @SerializedName("message") var message: String,
    /**
     * contains success status
     */
    @SerializedName("status") var status: String
)
