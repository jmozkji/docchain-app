package com.ufabc.docchain.data.models

import com.google.gson.annotations.SerializedName

data class FastApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: FastApiData
)
