package ghoudan.anfaSolution.com.networking.interceptor

import ghoudan.anfaSolution.com.utils.ApplicationInfoProvider
import dagger.Reusable
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

@Reusable
class DefaultHeadersInterceptor @Inject constructor(
    private val applicationInfoProvider: ApplicationInfoProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Content-Type", "application/json;charset=UTF-8")
            .header("platform", "android")
            .header("version", applicationInfoProvider.versionName)
            .header("deviceId", applicationInfoProvider.deviceId)
            .build()

        return chain.proceed(request)
    }
}
