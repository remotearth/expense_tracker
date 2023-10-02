package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseUser
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.contracts.LoginContract
import com.remotearthsolutions.expensetracker.databinding.ActivityLoginBinding
import com.remotearthsolutions.expensetracker.ui.LoginView
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseActivity(), View.OnClickListener,
    LoginContract.View {
    private val viewModel: LoginViewModel by viewModel { parametersOf(this, this) }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContent { LoginView(viewModel) }
        viewModel.init()
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
            val intent = Intent(this, InitialPreferenceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onLoginFailure() {
        showAlert(null, getString(R.string.login_failed), getString(R.string.ok), null, null, null)
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
