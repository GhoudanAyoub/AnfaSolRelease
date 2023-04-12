package ghoudan.anfaSolution.com.networking.interceptor

import android.content.SharedPreferences
import ghoudan.anfaSolution.com.utils.PreferencesModule
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

@Reusable
class AuthorizationInterceptor @Inject constructor(
    @Named(PreferencesModule.CURRENT_CREDENTIALS_PREFERENCES_NAME)
    private val userDataPreferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request =
            request.newBuilder()
                .header("Accept", "*/*")
                .header("Authorization", "Basic VXNlcjM6RGV2QVMyMkFuZmFT")
                .build()
        return chain.proceed(request)
    }
}
