package ghoudan.anfaSolution.com.networking.repository

import androidx.viewbinding.BuildConfig
import dagger.Reusable
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.request.LoginRequest
import ghoudan.anfaSolution.com.networking.state.UserCredentialsState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

@Reusable
class LoginRepository @Inject constructor(
    private val api: EpApi
) {
    suspend fun login(phoneNumber: String, password: String): Flow<UserCredentialsState> {
        val request = LoginRequest(phoneNumber, password)
        return flow<UserCredentialsState> {
            val result = api.login(request)
            emit(UserCredentialsState.Code(result.code()))
        }.onStart {
            emit(UserCredentialsState.Loading)
        }.catch { exception ->
            emit(UserCredentialsState.Error(exception))
        }
    }


    suspend fun sendSmsCode(phoneNumber: String): Flow<UserCredentialsState> {
        val sendCode = BuildConfig.DEBUG
        val request = LoginRequest(phoneNumber = phoneNumber, skipSendCode = sendCode)
        return flow<UserCredentialsState> {
            val result = api.sendSmsCode(request)
            emit(UserCredentialsState.Code(result.code()))
        }.onStart {
            emit(UserCredentialsState.Loading)
        }.catch { exception ->
            emit(UserCredentialsState.Error(exception))
        }
    }
}
