package com.example.thetask.api

import com.example.thetask.models.NextPath
import com.example.thetask.models.TaskInfo
import io.reactivex.Single

interface NetworkType {
    fun getNextPath(): Single<NextPath>
    fun getResponseCode(path: String): Single<TaskInfo>
}

object NetworkImpl : NetworkType {
    override fun getNextPath(): Single<NextPath> =
        RetrofitClient.retrofit.create(RetrofitService::class.java).getNextPath()

    override fun getResponseCode(path: String): Single<TaskInfo> =
        RetrofitClient.retrofit.create(RetrofitService::class.java).getTaskInfo(path)
}