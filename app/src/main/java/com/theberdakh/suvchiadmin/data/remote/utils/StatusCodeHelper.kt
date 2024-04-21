package com.theberdakh.suvchiadmin.data.remote.utils

fun Int.convertToMessage(): String {
    return when(this){
        200 -> "Soraw jetiskenlikli orınlandı"
        404 -> "Bunday maǵlıwmat tabılmadı"
        422 -> "Sorawda nadurıs maǵlıwmatlar bar"
        201 -> "Maǵlıwmat jetiskenlikli jaratıldı"
        500 -> "Serverdegi qátelik"
        409 -> "Duplikat maǵlıwmat"
        else -> "Qátelik kodı: $this"
    }
}