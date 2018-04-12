package edu.upi.cs.drake.tictaco.common

import android.arch.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import edu.upi.cs.drake.tictaco.model.User

interface UserService {
    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount)
    fun getUserLiveData(): LiveData<User>
    fun getCurrentUser(): User?
}