package edu.upi.cs.drake.tictaco.view.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import dagger.android.AndroidInjection
import edu.upi.cs.drake.tictaco.R
import edu.upi.cs.drake.tictaco.databinding.ActivityLoginBinding
import edu.upi.cs.drake.tictaco.view.mainmenu.MainActivity
import edu.upi.cs.drake.tictaco.viewmodel.LoginViewModel
import edu.upi.cs.drake.tictaco.viewmodel.ViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity(){

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var googleApiClient: GoogleApiClient

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val tag = "LoginActivity"
    private val requestCode = 7

    override fun onCreate(savedInstanceState: Bundle?){
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.getUserLiveData().observe(this, Observer{
            it?.let {
                startActivity(MainActivity.newInstance(this))
                finish()
            }
        })

        binding.signInButton.setOnClickListener{
            userSignInMethod()
        }
    }

    private fun userSignInMethod(){
        val authIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(authIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == this.requestCode){
            val googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(googleSignInResult.isSuccess){
                loginViewModel.signIn(googleSignInResult.signInAccount)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.disposeAll()
    }
}
