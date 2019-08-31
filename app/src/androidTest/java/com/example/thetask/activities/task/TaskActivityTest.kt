package com.example.thetask.activities.task

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.thetask.MyApplication
import com.example.thetask.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Provider

@SmallTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TaskActivityTest {

    @Mock
    lateinit var presenter1: TaskContract.Presenter<TaskContract.View>

    @Rule
    @JvmField
    var activityRule =
        object : ActivityTestRule<TaskActivity>(TaskActivity::class.java, false, false) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                val myApp =
                    InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApplication
                myApp.dispatchingActivityInjector = createFakeTaskActivityInjector {
                    presenter = presenter1
                }
            }
        }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        activityRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun testBasicSetup() {
        onView(ViewMatchers.withText("Fetch Content")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.fetchContentButton)).perform(click())
    }


    private fun createFakeTaskActivityInjector(block: TaskActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {
        val injector = AndroidInjector<Activity> { instance ->
            if (instance is TaskActivity) {
                instance.block()
            }
        }
        val factory = AndroidInjector.Factory<Activity> { injector }
        val map = mapOf(
            Pair<Class<*>,
                    Provider<AndroidInjector.Factory<*>>>(
                TaskActivity::class.java,
                Provider { factory })
        )
        return DispatchingAndroidInjector_Factory.newInstance(map, emptyMap())
    }
}