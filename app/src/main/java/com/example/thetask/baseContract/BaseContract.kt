package com.example.thetask.baseContract

/**
 * A BaseContract that defines a basic blueprint of Presenter and View
 */
interface BaseContract {

    interface Presenter<T: View> {

        val view: T?

        /**
         * Should be called from an activity or a fragment to attach the view instance
         * Usually called in onCreate() or onCreateView() or onResume() lifecycle callback methods
         */
        fun attachView(view: T)

        /**
         * Should be called from an activity or a fragment to detach the view from the presenter.
         * Usually called in onStop() or onDestroy() or onDestroyView() lifecycle callback methods
         */
        fun detachView()
    }

    interface View
}