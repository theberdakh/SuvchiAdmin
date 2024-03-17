package com.theberdakh.suvchiadmin.data.remote

data class DataResponse<T>(
    val data: List<T>,
    val count: Int
)
