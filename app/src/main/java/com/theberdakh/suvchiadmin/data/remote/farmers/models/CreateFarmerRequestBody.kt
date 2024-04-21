package com.theberdakh.suvchiadmin.data.remote.farmers.models

/*"firstName": "Azizbek",
    "lastName": "Berdimuratov",
    "middleName": "Maxsetbayevichcv",
    "phone": "998912672430",
    "password": "Password:123",
    "username": "googles",
    "latitude": "42.460411",
    "longitude": "59.605049",
    "passport": "AD0166520",
    "gender": "male", //male, fale,
    "regionId": 1*/

data class CreateFarmerRequestBody(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phone: String,
    val password: String,
    val username: String,
    val latitude: String,
    val longitude: String,
    val passport: String,
    val gender: String,
    val regionId: Int,
    val height: Int,
    val alpha: Int,
    val beta: Int,
    val bottom_length: Int
)