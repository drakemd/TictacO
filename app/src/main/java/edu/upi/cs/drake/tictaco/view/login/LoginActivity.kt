/**
 * Copyright 2018 Indraga Martiyana D
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * this activity is responsible in handling login view
 * it wont be displayed if the user is already logged in
 */
class LoginActivity : AppCompatActivity(){

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var googleApiClient: GoogleApiClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    //request code for google sign in can be replace with any number (not sure)
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

        binding.signInButton.setOnClickListener{userSignInMethod()}
    }

    //this method will display google sign in popup
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
