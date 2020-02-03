package ru.shrott.shrottmaster.other.di

import dagger.Component
import ru.shrott.shrottmaster.model.ModelImpl
import ru.shrott.shrottmaster.view.fragments.BadgeListFragment
import ru.shrott.shrottmaster.view.fragments.PassFragment
import ru.shrott.shrottmaster.view_model.*
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ModelModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(data: ModelImpl)
    fun inject(mastersViewModel: MastersViewModel)
    fun inject(badgesViewModel: BadgesViewModel)
    fun inject(passViewModel: PassViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(badgeListFragment: BadgeListFragment)
    fun inject(passFragment: PassFragment)
    fun inject(settingsViewModel: SettingsViewModel)
}