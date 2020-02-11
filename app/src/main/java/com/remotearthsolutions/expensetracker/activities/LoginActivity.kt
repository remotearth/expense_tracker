package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.LoginContract
import com.remotearthsolutions.expensetracker.services.FacebookServiceImpl
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.services.GoogleServiceImpl
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener,
    LoginContract.View {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                LoginViewModel(
                    this,
                    this,
                    GoogleServiceImpl(this),
                    FacebookServiceImpl(this),
                    FirebaseServiceImpl(this)
                )
            }).get(LoginViewModel::class.java).apply {
                this.init()
            }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.facebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GOOGLE_SIGNIN_INTENT) {
            viewModel.googleLoginWithIntent(data)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.googlebutton -> viewModel.startGoogleLogin()
            R.id.facebook_login_button -> viewModel.startFacebookLogin()
            R.id.withoutloginbutton -> onLoginSuccess(null)
        }
    }

    override fun initializeView() {
        googlebutton.setOnClickListener(this)
        facebook_login_button.setOnClickListener(this)
        withoutloginbutton.setOnClickListener(this)
    }

    override fun onLoginSuccess(user: FirebaseUser?) {
        if (user == null) {
            SharedPreferenceUtils.getInstance(this)
                ?.putString(Constants.KEY_USER, "guest")
        } else {
            println(user.uid)
            SharedPreferenceUtils.getInstance(this)
                ?.putString(Constants.KEY_USER, user.uid)
        }

        if (SharedPreferenceUtils.getInstance(this)?.getBoolean(
                Constants.PREF_ISFIRSTTIMEVISITED,
                false
            )!!
        ) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val intent = Intent(this, CurrencySelectionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onLoginFailure() {
        showAlert(null, getString(R.string.login_failed), getString(R.string.ok), null, null)
    }

    override fun loadUserEmails() {
        val mGoogleSignInClient = viewModel.googleSignInClient
        if (mGoogleSignInClient == null) {
            Log.w(
                TAG,
                getString(R.string.mGoogleSignInClient_must_be_initialized)
            )
            return
        }
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            REQUEST_CODE_GOOGLE_SIGNIN_INTENT
        )
    }

    companion object {
        private val TAG = LoginActivity::class.java.name
        private const val REQUEST_CODE_GOOGLE_SIGNIN_INTENT = 101
    }
}