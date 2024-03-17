package com.theberdakh.suvchiadmin.data.remote.contract.models

data class CreateContractResponse(
    val title: String,
    val fileId: Int,
    val id: Int,
    val createdAt: String
)