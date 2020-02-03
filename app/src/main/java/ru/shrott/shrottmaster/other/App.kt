package ru.shrott.shrottmaster.other

import android.app.Application
import android.content.Context
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.other.di.AppComponent
import ru.shrott.shrottmaster.other.di.AppModule
import ru.shrott.shrottmaster.other.di.DaggerAppComponent
import ru.shrott.shrottmaster.other.di.ModelModule


@ReportsCrashes(
    mailTo = "albu28@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_toast_text
)

class App : Application() {

    companion object {
        private var component: AppComponent? = null
        fun getComponent(): AppComponent? {
            return component
        }
    }

    override fun onCreate() {
        super.onCreate()
        component = buildComponent()
        ACRA.init(this)
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .modelModule(ModelModule())
            .appModule(AppModule(this))
            .build()
    }


    override fun attachBaseContext(base: Context) {
        try {
            super.attachBaseContext(base)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }

    }
}