package edu.upi.cs.drake.tictaco.common

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import edu.upi.cs.drake.tictaco.model.User

class FirebaseUserService: UserService{

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