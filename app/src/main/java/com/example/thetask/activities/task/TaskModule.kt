package com.example.thetask.activities.task

import android.content.Context
import android.content.SharedPreferences
import com.example.thetask.MyApplication
import com.example.thetask.api.NetworkImpl
import com.example.thetask.api.NetworkType
import dagger.Module
import dagger.Provides


@Module
class TaskModule {
    @Provides
    fun provideTaskPresenter(networkType: NetworkType, sharedPreferences: SharedPreferences): TaskContract.Presenter<TaskContract.View>
            = TaskPresenter(networkType, sharedPreferences)

    @Provides
    fun provideNetworkingService(): NetworkType = NetworkImpl

    @Provides
    fun provideContext(application: MyApplication): Context {
        return application
    }

    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences = context.getSharedPreferences("TimesFetched", Context.MODE_PRIVATE)
}