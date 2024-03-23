package com.theberdakh.suvchiadmin.data.remote.contract

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.contract.models.ContractByFarmerId
import com.theberdakh.suvchiadmin.data.remote.contract.models.CreateContractRequestBody
import com.theberdakh.suvchiadmin.data.remote.contract.models.CreateContractResponse
import com.theberdakh.suvchiadmin.data.remote.contract.models.UploadFileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ContractsApi {

    @GET("/admin/contract")
    suspend fun getAllContracts(): Response<DataResponse<Contract>>

    @Multipart
    @POST("/local-files/upload")
    suspend fun uploadContractFile(@Part body: MultipartBody.Part): Response<UploadFileResponse>

    @POST("/admin/contract")
    suspend fun createContract(@Body createContractBody: CreateContractRequestBody): Response<CreateContractResponse>

    @GET("/admin/contract/fermer/{farmerId}?sort[contractUser.id]=DESC")
    suspend fun getAllContractsByFarmerid(@Path("farmerId") farmerId: Int): Response<DataResponse<ContractByFarmerId>>

}