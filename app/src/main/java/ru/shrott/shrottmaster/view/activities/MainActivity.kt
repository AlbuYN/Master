package ru.shrott.shrottmaster.view.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view_model.MainViewModel

class MainActivity : AppCompatActivity(), MainActivityCallback {

    private var navController: NavController? = null
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        lifecycle.addObserver(viewModel!!)

        viewModel!!.errorMessage.observe(this, Observer { errorMessage ->  errorMessage?.let { showError(it) } })

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        //val token = FirebaseInstanceId.getInstance().token
        //Log.d("myLog", "FCM Registration Token: $token" )


    }

    override fun loadNomenclature() {
        viewModel?.loadNomenclature()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let { it.childFragmentManager
                .primaryNavigationFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun openSettings() {
        navController?.navigate(R.id.action_mastersFragment_to_settingsFragment)
    }

    private fun showError(errorMessage: String) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun openTotalPercentFragment(args: Bundle) {
        navController?.navigate(R.id.action_passFragment_to_totalPercentFragment, args)
    }
}
