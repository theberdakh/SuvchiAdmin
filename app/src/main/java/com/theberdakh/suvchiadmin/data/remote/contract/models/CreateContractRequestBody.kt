package com.theberdakh.suvchiadmin.data.remote.contract.models

data class CreateContractRequestBody(
    val title: String,
    val fileId:Int,
    val userId: List<Int>
)
