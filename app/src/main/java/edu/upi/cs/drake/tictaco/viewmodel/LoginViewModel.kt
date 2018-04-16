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

package edu.upi.cs.drake.tictaco.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import edu.upi.cs.drake.tictaco.firestore.UserService
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userService: UserService): ViewModel(){

    private var userLiveData = userService.getUserLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun getUserLiveData() = userLiveData

    fun signIn(googleSignInAccount: GoogleSignInAccount?){
        googleSignInAccount?.let{userService.signInWithGoogle(googleSignInAccount)}
    }

    fun disposeAll(){
        compositeDisposable.dispose()
    }
}