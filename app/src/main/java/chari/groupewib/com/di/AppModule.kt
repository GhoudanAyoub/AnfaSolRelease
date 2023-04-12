package ghoudan.anfaSolution.com.di

import android.content.SharedPreferences
import ghoudan.anfaSolution.com.BuildConfig
import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.di.CurrentUserProvider
import ghoudan.anfaSolution.com.networking.di.CustomerProvider
import ghoudan.anfaSolution.com.utils.ApplicationInfoProvider
import ghoudan.anfaSolution.com.utils.Constants
import ghoudan.anfaSolution.com.utils.PreferencesModule
import ghoudan.anfaSolution.com.utils.PreferencesModule.DEVICE_ID_KEY
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.UUID
import javax.inject.Named
import kotlinx.serialization.json.Json


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Reusable
    fun provideAppInfo(
        @Named(PreferencesModule.DEVICE_ID_PREFERENCE_NAME)
        deviceIdPreferences: SharedPreferences,
    ) = object : ApplicationInfoProvider {
        override val versionName: String
            get() = BuildConfig.VERSION_NAME

        override val deviceId: String
            get() {
                val deviceIdPreference = deviceIdPreferences.getString(
                    DEVICE_ID_KEY,
                    ""
                )
                val deviceId = if (!deviceIdPreference.isNullOrEmpty()) {
                    deviceIdPreference
                } else {
                    UUID.randomUUID().toString()
                }

                deviceIdPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply()
                return deviceId
            }
    }

    @Provides
    @Reusable
    fun provideCurrentUserInfo(
        @Named(PreferencesModule.CURRENT_USER_PREFERENCES_NAME)
        currentUserPreferences: SharedPreferences,
    ): CurrentUserProvider {

        return object : CurrentUserProvider {
            override fun currentUser(): User? {
                val currentUserInfo =
                    currentUserPreferences.getString(Constants.CURRENT_USER_KEY, "") ?: ""
                return try {
                    Json.decodeFromString(
                        User.serializer(), currentUserInfo
                    )
                } catch (e: Exception) {
                    return null
                }
            }
        }
    }

    @Provides
    @Reusable
    fun provideCurrentCustomer(
        currentUserProvider: CurrentUserProvider,
        @Named(PreferencesModule.SELECTED_CUSTOMER_PREFERENCES_NAME)
        selectedCustomerPreferences: SharedPreferences
    ): CustomerProvider {
        return object : CustomerProvider {
            override fun selectedCustomer(): Customer? {
                val currentUser = currentUserProvider.currentUser()
//                currentUser?.let { user ->
//                    when (user.userTypeId) {
//                        UserTypeId.CUSTOMER.id -> {
//                            return user.customer
//                        }
//                        UserTypeId.AMBASSADOR.id -> {
//                            val selectedCustomerJson =
//                                selectedCustomerPreferences.getString(
//                                    Constants.CURRENT_CUSTOMER_KEY, ""
//                                ) ?: ""
//                            return try {
//                                Json.decodeFromString(
//                                    Customer.serializer(), selectedCustomerJson
//                                )
//                            } catch (e: Exception) {
//                                return null
//                            }
//                        }
//                        else -> {
//                            return null
//                        }
//                    }
//                }
                val selectedCustomerJson =
                    selectedCustomerPreferences.getString(
                        Constants.CURRENT_CUSTOMER_KEY, ""
                    ) ?: ""
                return try {
                    Json.decodeFromString(
                        Customer.serializer(), selectedCustomerJson
                    )
                } catch (e: Exception) {
                    return null
                }
            }
        }
    }

    @Provides
    @Reusable
    fun provideJson(): Json {
        return Json { encodeDefaults = true }
    }
}
