package ghoudan.anfaSolution.com

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber


@HiltAndroidApp
class AnfaAPP : Application(), androidx.work.Configuration.Provider {


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {

        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
