package com.voviihb.dz2

import com.google.gson.annotations.SerializedName

data class DogImage(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status  : String
)
