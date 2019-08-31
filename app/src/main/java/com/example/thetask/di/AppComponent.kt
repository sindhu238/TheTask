package com.example.thetask.di

import com.example.thetask.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityBuilder::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MyApplication)
}