package ru.shrott.shrottmaster.other.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ru.shrott.shrottmaster.model.Model
import ru.shrott.shrottmaster.model.ModelImpl

@Module
class ViewModelModule {
    @Provides internal fun compositeDisposable() : CompositeDisposable {return CompositeDisposable() }
    @Provides internal fun provideModel(): Model {return ModelImpl()}
}