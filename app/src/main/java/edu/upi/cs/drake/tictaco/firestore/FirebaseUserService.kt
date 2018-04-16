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

package edu.upi.cs.drake.tictaco.firestore

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import edu.upi.cs.drake.tictaco.model.User

/**
 * helper class to manage firebase user data and auth
 */
class FirebaseUserService: UserService {

    private var userData: MutableLiveData<User> = MutableLiveData()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener{
        firebaseAuth?.currentUser?.let{
            userData.value = User(it.uid, it.displayName, it.photoUrl.toString(), it.email)
        }
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        firebaseAuth.signInWithCredential(authCredential)
    }

    override fun getUserLiveData() = userData
    override fun getCurrentUser() = userData.value
}