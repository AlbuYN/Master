package ru.shrott.shrottmaster.other.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.shrott.shrottmaster.model.api.ApiFactory
import ru.shrott.shrottmaster.model.api.ApiInterface
import ru.shrott.shrottmaster.other.Const
import javax.inject.Named
import javax.inject.Singleton


@Module
class ModelModule {
    @Provides
    @Singleton
    internal fun provideApiInterface(): ApiInterface {
        return ApiFactory.getApiInterface(Const.BASE_URL)
    }

    @Singleton
    @Provides @Named(Const.UI_THREAD)
    internal fun provideSchedulerUI(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Singleton
    @Provides @Named(Const.IO_THREAD)
    internal fun provideSchedulerIO(): Scheduler {
        return Schedulers.io()
    }
}