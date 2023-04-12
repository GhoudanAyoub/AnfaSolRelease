package ghoudan.anfaSolution.com.networking.di

import android.app.Application
import androidx.room.Room
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.interceptor.AuthorizationInterceptor
import ghoudan.anfaSolution.com.networking.interceptor.DefaultHeadersInterceptor
import ghoudan.anfaSolution.com.networking.interceptor.ErrorHandlingInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moczul.ok2curl.CurlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber


@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {


    @Singleton
    @Provides
    fun provideApiHolder() = ApiHolder()

    @Singleton
    @Provides
    internal fun providesOkHttp(
        errorHandlingInterceptor: ErrorHandlingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
        defaultHeadersInterceptor: DefaultHeadersInterceptor
    ): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(defaultHeadersInterceptor)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(CurlInterceptor {
                Timber.d("Ok2Curl $it ")
            })
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorHandlingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    }


    @Singleton
    @Provides
    fun providesEpApi(httpClient: OkHttpClient, apiHolder: ApiHolder): EpApi {
        val contentType = "application/json".toMediaType()
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }

        val api = Retrofit.Builder()
            .baseUrl("http://197.230.127.119:7048/Mobile_app/ODataV4/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpClient)
            .build()
            .create(EpApi::class.java)

        apiHolder.api = api
        return api
    }


    @Provides
    @Singleton
    fun provideDatabase(app: Application): RoomDataBase =
        Room.databaseBuilder(app, RoomDataBase::class.java, "dbAnfa")
            .build()


    @Singleton
    @Provides
    fun provideYourDao(db: RoomDataBase) = db.roomDao()

}
