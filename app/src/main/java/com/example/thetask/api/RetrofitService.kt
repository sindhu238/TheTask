package com.example.thetask.api

import com.example.thetask.models.NextPath
import com.example.thetask.models.TaskInfo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Single
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("./")
    fun getNextPath(): Single<NextPath>

    @GET("{path}")
    fun getTaskInfo(@Path("path", encoded = true) path: String): Single<TaskInfo>
}

object RetrofitClient {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://10.0.2.2:8000")
            .client(client)
            .build()
    }
}
