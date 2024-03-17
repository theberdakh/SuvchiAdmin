package com.theberdakh.suvchiadmin.data.remote.contract.models

data class UploadFileResponse(
    val filename: String,
    val path: String,
    val mimetype: String,
    val id: Int
)
