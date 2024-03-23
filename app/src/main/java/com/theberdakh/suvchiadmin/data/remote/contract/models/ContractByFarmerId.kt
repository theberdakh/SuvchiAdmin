package com.theberdakh.suvchiadmin.data.remote.contract.models

data class ContractByFarmerId(
    val id: Int,
    val report: String?,
    val userId: Int,
    val contractId: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val contract: CreateContractResponse
)