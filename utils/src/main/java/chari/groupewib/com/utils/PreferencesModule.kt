package ghoudan.anfaSolution.com.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Named(CURRENT_CREDENTIALS_PREFERENCES_NAME)
    @Singleton
    fun provideUserCredentialsPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            CURRENT_CREDENTIALS_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named(CURRENT_USER_PREFERENCES_NAME)
    @Singleton
    fun provideCurrentPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            CURRENT_USER_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named(SELECTED_CUSTOMER_PREFERENCES_NAME)
    @Singleton
    fun provideSelectedCustomerPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            SELECTED_CUSTOMER_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Deprecated(
        "legacy data",
        replaceWith = ReplaceWith("provideCurrentPreferences")
    )
    @Provides
    @Singleton
    @Named(LEGACY_USER_DATA_PREFERENCES)
    fun provideLegacyUserInfo(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            LEGACY_USER_DATA_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named(CURRENT_LANGUAGE_NAME)
    @Singleton
    fun provideCurrentLanguage(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            CURRENT_LANGUAGE_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named(SEARCH_HISTORY_PREFERENCES)
    @Singleton
    fun provideSearchHistoryPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            SEARCH_HISTORY_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Named(DEVICE_ID_PREFERENCE_NAME)
    @Singleton
    fun provideDeviceIdPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            DEVICE_ID_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    const val CURRENT_CREDENTIALS_PREFERENCES_NAME = "current credentials preferences"
    const val CURRENT_USER_PREFERENCES_NAME = "current user preferences"
    const val SELECTED_CUSTOMER_PREFERENCES_NAME = "selected customer"
    const val CURRENT_LANGUAGE_NAME = "current language preferences"
    const val SEARCH_HISTORY_PREFERENCES = "search_history"
    const val DEVICE_ID_PREFERENCE_NAME = "device id preference"

    const val DEVICE_ID_KEY = "device_id"

    @Deprecated("legacy data", replaceWith = ReplaceWith(CURRENT_CREDENTIALS_PREFERENCES_NAME))
    const val LEGACY_USER_DATA_PREFERENCES = "User_Data"
}
