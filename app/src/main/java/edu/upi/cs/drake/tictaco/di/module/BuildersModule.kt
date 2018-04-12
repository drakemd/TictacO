package edu.upi.cs.drake.tictaco.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import edu.upi.cs.drake.tictaco.view.game.GameActivity
import edu.upi.cs.drake.tictaco.view.login.LoginActivity
import edu.upi.cs.drake.tictaco.view.mainmenu.MainActivity

@Module
abstract class BuildersModule {
    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindGameActivity(): GameActivity
}