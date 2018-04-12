package edu.upi.cs.drake.tictaco.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import edu.upi.cs.drake.tictaco.viewmodel.GameViewModel
import edu.upi.cs.drake.tictaco.viewmodel.LoginViewModel
import edu.upi.cs.drake.tictaco.viewmodel.MainMenuViewModel
import edu.upi.cs.drake.tictaco.viewmodel.ViewModelFactory

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainMenuViewModel::class)
    internal abstract fun bindMainMenuViewModel(mainMenuViewModel: MainMenuViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    internal abstract fun bindGameViewModel(gameViewModel: GameViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}