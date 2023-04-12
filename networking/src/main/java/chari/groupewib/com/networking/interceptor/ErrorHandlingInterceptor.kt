package ghoudan.anfaSolution.com.networking.interceptor

import ghoudan.anfaSolution.com.networking.exception.HttpBadRequestException
import ghoudan.anfaSolution.com.networking.exception.HttpConflictException
import ghoudan.anfaSolution.com.networking.exception.HttpForbiddenException
import ghoudan.anfaSolution.com.networking.exception.HttpInternalErrorException
import ghoudan.anfaSolution.com.networking.exception.HttpLockedException
import ghoudan.anfaSolution.com.networking.exception.HttpNoInternetException
import ghoudan.anfaSolution.com.networking.exception.HttpNotAcceptableException
import ghoudan.anfaSolution.com.networking.exception.HttpNotFoundException
import dagger.Reusable
import java.net.HttpURLConnection
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * Transforms networking errors and status codes into catchable and recognizable exceptions.
 */
@Reusable
internal class ErrorHandlingInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val code = response.code
        chain.request().url.toString()

        when (code) {
            HttpURLConnection.HTTP_NO_CONTENT -> Timber.e("NO_CONTENT")
            HttpURLConnection.HTTP_UNAUTHORIZED -> Timber.e("UNAUTHORIZED")
            HttpURLConnection.HTTP_BAD_REQUEST -> Timber.e("HTTP_BAD_REQUEST")
            HttpURLConnection.HTTP_INTERNAL_ERROR -> throw HttpInternalErrorException()
            HttpURLConnection.HTTP_NOT_FOUND -> Timber.e("HttpNotFoundException")
            HttpURLConnection.HTTP_CONFLICT -> throw HttpConflictException()
            HttpURLConnection.HTTP_FORBIDDEN -> throw HttpForbiddenException()
            HttpURLConnection.HTTP_NOT_ACCEPTABLE -> throw HttpNotAcceptableException()
            HttpURLConnection.HTTP_BAD_GATEWAY, HttpURLConnection.HTTP_UNAVAILABLE ->
                throw HttpNoInternetException()
            423 -> throw HttpLockedException()
        }

        return response
    }
}
