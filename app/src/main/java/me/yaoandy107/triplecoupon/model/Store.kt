package me.yaoandy107.triplecoupon.model

data class Store(
    val hsnCd: String,
    val hsnNm: String,
    val townCd: String?,
    val townNm: String,
    val storeCd: String,
    val storeNm: String,
    val addr: String,
    val zipCd: String,
    val tel: String,
    val busiTime: String,
    val busiMemo: String?,
    val longitude: String,
    val latitude: String,
    val total: String,
    val updateDate: String
)