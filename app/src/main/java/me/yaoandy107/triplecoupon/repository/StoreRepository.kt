package me.yaoandy107.triplecoupon.repository

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.yaoandy107.triplecoupon.model.Store
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class StoreRepository {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }
    private val request by lazy {
        Request.Builder()
            .url("https://3000.gov.tw/hpgapi-openmap/api/getPostData")
            .build()
    }

    fun getStores(): Flow<List<Store>> {
        return flow {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val itemType = object : TypeToken<List<Store>>() {}.type
            val gson = GsonBuilder().create()
            val stores: List<Store> = gson.fromJson(body, itemType)
            emit(stores)
        }.flowOn(Dispatchers.IO)
    }
}