package edu.upi.cs.drake.tictaco.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import edu.upi.cs.drake.tictaco.TicTacOApp
import edu.upi.cs.drake.tictaco.di.module.AppModule
import edu.upi.cs.drake.tictaco.di.module.BuildersModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, BuildersModule::class])
interface AppComponent {
    @Component.Builder
    interface Builders{
        @BindsInstance
        fun application(app: TicTacOApp): Builders
        fun build(): AppComponent
    }
    fun inject(app: TicTacOApp)
}