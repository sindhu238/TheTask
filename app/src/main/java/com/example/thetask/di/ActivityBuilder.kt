package com.example.thetask.di

import com.example.thetask.activities.task.TaskActivity
import com.example.thetask.activities.task.TaskModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [TaskModule::class])
    internal abstract fun bindActivity(): TaskActivity
}


