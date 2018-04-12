package edu.upi.cs.drake.tictaco.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import edu.upi.cs.drake.tictaco.common.UserService
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