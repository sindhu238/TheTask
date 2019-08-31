package com.example.thetask.baseContract

abstract class BasePresenter<T: BaseContract.View> : BaseContract.Presenter<T> {

    override var view: T? = null

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}