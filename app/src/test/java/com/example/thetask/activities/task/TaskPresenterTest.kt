package com.example.thetask.activities.task

import android.content.SharedPreferences
import com.example.thetask.api.NetworkType
import com.example.thetask.models.NextPath
import com.example.thetask.models.TaskInfo
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class TaskPresenterTest {

    @Mock
    lateinit var network: NetworkType

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Mock
    lateinit var view: TaskContract.View

    @Mock
    lateinit var editor: SharedPreferences.Editor

    lateinit var presenter: TaskPresenter

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        MockitoAnnotations.initMocks(this)
        presenter = TaskPresenter(network, sharedPreferences)
    }

    @After
    fun tearDown() {
        presenter.detachView()
    }

    @Test
    fun `test attach`() {
        TestCase.assertNull(presenter.view)
        presenter.attachView(view)
        TestCase.assertNotNull(presenter.view)
    }

    @Test
    fun `test detach`() {
        presenter.attachView(view)
        presenter.detachView()
        TestCase.assertNull(presenter.view)
    }

    @Test
    fun `test updating of response code and response times - When Internet is Connected`() {
        // Arrange
        mockSetup()
        whenever(view.isConnected).thenReturn(true)

        // Act
        presenter.attachView(view)

        // Assert
        verify(view).updateData("Sample Code", 1)
        verify(view, never()).displaySnackBar(any())
    }

    @Test
    fun `test error connection`() {
        // Arrange
        val testPath = "errorPath"
        val testResponseCode = "Sample Code"
        whenever(network.getNextPath()).thenReturn(Single.just(NextPath(testPath)))
        whenever(network.getResponseCode("d")).thenReturn(Single.just(TaskInfo(testPath, testResponseCode)))
        whenever(view.isConnected).thenReturn(true)

        // Act
        presenter.attachView(view)

        // Assert
        verify(view, never()).updateData(any(), any())
        verify(view).displaySnackBar(any())
    }

    @Test
    fun `test no interactions - Internet Disconnected`() {
        // Arrange
        mockSetup()
        whenever(view.isConnected).thenReturn(false)

        // Act
        presenter.attachView(view)

        // Assert
        verify(view, never()).updateData(any(), any())
        verify(view).displaySnackBar(any())
    }

    private val immediate = object : Scheduler() {
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
        }
    }

    private fun mockSetup() {
        val testPath = "http://localhost:8000/d/"
        val testResponseCode = "Sample Code"
        whenever(network.getNextPath()).thenReturn(Single.just(NextPath(testPath)))
        whenever(network.getResponseCode("/d/")).thenReturn(Single.just(TaskInfo(testPath, testResponseCode)))
        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(sharedPreferences.getInt("count", 0)).thenReturn(0)
        whenever(editor.putInt("count", 1)).thenReturn(editor)
    }
}