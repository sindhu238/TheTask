package com.example.thetask.activities.task

import android.content.SharedPreferences
import com.example.thetask.api.NetworkType
import com.example.thetask.baseContract.BaseContract

interface TaskContract: BaseContract {
    interface View: BaseContract.View {
        val isConnected: Boolean

        fun updateData(code: String, count: Int)
        fun displaySnackBar()
    }

    interface Presenter<T: View>: BaseContract.Presenter<T> {
        val network: NetworkType
        val sharedPreference: SharedPreferences

        fun getResponseCode()
    }
}