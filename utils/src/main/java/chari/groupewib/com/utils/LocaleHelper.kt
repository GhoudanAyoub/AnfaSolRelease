package ghoudan.anfaSolution.com.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class LocaleHelper @Inject constructor(
    @Named(PreferencesModule.CURRENT_LANGUAGE_NAME)
    private var languagePreferences: SharedPreferences,
    @ApplicationContext private var context: Context
) {

    companion object {

        fun replaceArabicNumbers(original: String): String {
            return original.replace("١".toRegex(), "1")
                .replace("٢".toRegex(), "2")
                .replace("٣".toRegex(), "3")
                .replace("٤".toRegex(), "4")
                .replace("٥".toRegex(), "5")
                .replace("٦".toRegex(), "6")
                .replace("٧".toRegex(), "7")
                .replace("٨".toRegex(), "8")
                .replace("٩".toRegex(), "9")
                .replace("٠".toRegex(), "0")
                .replace(",".toRegex(), ".")
                .replace("،".toRegex(), ".")

        }

        fun setupLanguage(context: Context): Context {
            val languagePreferences = context.getSharedPreferences(
                PreferencesModule.CURRENT_LANGUAGE_NAME,
                MODE_PRIVATE
            )
            var lang = languagePreferences.getString(Constants.CURRENT_LANGUAGE, "fr")
            Timber.e("setupLanguage $lang")
            if (lang.isNullOrEmpty()) {
                lang = "fr"
            }
            languagePreferences.edit().putString(Constants.CURRENT_LANGUAGE, lang).apply()

            val res = context.resources
            val config = res.configuration

            val locale = Locale(lang, "MA")
            if (lang == "ar") {
                Locale.setDefault(locale)
                config.setLocale(locale)
                config.setLayoutDirection(Locale(lang, "MA"))
            } else {
                Locale.setDefault(locale)
                config.setLocale(locale)
                config.setLayoutDirection(Locale.FRENCH)
            }
            res.updateConfiguration(config, res.displayMetrics)

            return context.createConfigurationContext(config)
        }

        fun getLanguage(context: Context): String {
            val languagePreferences = context.getSharedPreferences(
                PreferencesModule.CURRENT_LANGUAGE_NAME,
                MODE_PRIVATE
            )
            return languagePreferences.getString(Constants.CURRENT_LANGUAGE, "fr") ?: "fr"
        }
    }

    fun getLanguage(): String {
        return languagePreferences.getString(Constants.CURRENT_LANGUAGE, "fr") ?: "fr"
    }

    fun setLanguage(
        lang: String
    ): Context {
        Timber.d("setLanguage $lang")
        languagePreferences.edit().putString(Constants.CURRENT_LANGUAGE, lang).apply()
        Timber.d(
            "setLanguage - preferences ${
                languagePreferences.getString(
                    Constants.CURRENT_LANGUAGE,
                    ""
                )
            }"
        )
        val locale = Locale(lang)
        val config = context.resources.configuration
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            val resources = context.resources
            resources.updateConfiguration(config, resources.displayMetrics)
            context
        }
    }

    fun isRtl(): Boolean {
        return when (getLanguage()) {
            "ar" -> {
                true
            }
            "fr" -> {
                false
            }
            else -> {
                false
            }
        }
    }
}
