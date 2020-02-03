package ru.shrott.shrottmaster.other.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.shrott.shrottmaster.model.PreferenceHelper
import ru.shrott.shrottmaster.model.database.AppDatabase
import ru.shrott.shrottmaster.other.Utils.Permissions
import ru.shrott.shrottmaster.other.Utils.Utils
import ru.shrott.shrottmaster.other.Utils.WorkWithPhotos
import javax.inject.Singleton



@Module
class AppModule(_appContext: Context) {
    var appContext : Context = _appContext

    @Provides @Singleton internal fun provideContext(): Context { return appContext }
    @Provides @Singleton internal fun permissions(): Permissions { return Permissions() }
    @Provides @Singleton internal fun preferenceHelper():
            PreferenceHelper {return PreferenceHelper(appContext)}
    @Provides @Singleton internal fun appDatabase(): AppDatabase {
        return Room.databaseBuilder(appContext.applicationContext, AppDatabase::class.java, "database").build()}
    @Provides @Singleton internal fun utils(): Utils {
        return Utils(appContext)
    }
    @Provides @Singleton internal fun workWithPhotos(): WorkWithPhotos {
        return WorkWithPhotos(appContext)
    }

}