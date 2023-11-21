package com.voviihb.dz2

import com.google.gson.annotations.SerializedName

data class DogImage(
//  contains imageUrl
    @SerializedName("message") var message: String,
//  contains success or not
    @SerializedName("status") var status: String
)
