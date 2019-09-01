package com.example.thetask.activities.task

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.thetask.R
import com.example.thetask.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class TaskActivity : DaggerAppCompatActivity(), TaskContract.View {

    @Inject
    lateinit var presenter: TaskContract.Presenter<TaskContract.View>

    private lateinit var binding: ActivityMainBinding

    override val isConnected: Boolean
    get() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.presenter = presenter
        presenter.attachView(this)
    }

    override fun updateData(code: String, count: Int) {
        responseCodeTV.text = code
        timesFetchedTV.text = "$count"
    }

    override fun displaySnackBar(strResource: Int) {
        Snackbar.make(binding.containerView, getString(strResource), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
