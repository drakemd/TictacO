package edu.upi.cs.drake.tictaco.di.module

import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import dagger.Module
import dagger.Provides
import edu.upi.cs.drake.tictaco.R
import edu.upi.cs.drake.tictaco.TicTacOApp
import edu.upi.cs.drake.tictaco.common.FirebaseUserService
import edu.upi.cs.drake.tictaco.common.FirestoreGameHelper
import edu.upi.cs.drake.tictaco.common.FirestoreHelper
import edu.upi.cs.drake.tictaco.common.UserService
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideAppContext(app: TicTacOApp): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideGoogleApiClient(context: Context): GoogleApiClient{
        // Creating and Configuring Google Sign In object.
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_key))
                .requestEmail()
                .build()

        // Creating and Configuring Google Api Client.
        return GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOption)
                .build()
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService = FirebaseUserService()

    @Singleton
    @Provides
    fun provideFirestore(): FirestoreHelper = FirestoreGameHelper()
}