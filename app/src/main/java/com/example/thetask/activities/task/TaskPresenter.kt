package com.example.thetask.activities.task

import android.content.SharedPreferences
import com.example.thetask.api.NetworkType
import com.example.thetask.baseContract.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.net.URI
import javax.inject.Inject

class TaskPresenter @Inject constructor(
    override val network: NetworkType,
    override val sharedPreference: SharedPreferences
) : BasePresenter<TaskContract.View>(), TaskContract.Presenter<TaskContract.View> {

    private val bag = CompositeDisposable()

    override fun attachView(view: TaskContract.View) {
        super.attachView(view)
        getResponseCode()
    }

    override fun getResponseCode() {
        if (view?.isConnected == true) {
            network.getNextPath()
                .subscribeOn(Schedulers.io())
                .map {
                    URI.create(it.next_path).path ?: ""
                }
                .flatMap {
                    network.getResponseCode(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { nextPath ->
                    sharedPreference.apply {
                        val count = getInt("count", 0) + 1
                        edit().putInt("count", count).apply()
                        view?.updateData(nextPath.response_code, count)
                    }
                }.addTo(bag)
        } else {
            view?.displaySnackBar()
        }
    }

    override fun detachView() {
        super.detachView()
        bag.clear()
    }
}