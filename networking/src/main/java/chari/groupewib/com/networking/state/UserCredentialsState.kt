package ghoudan.anfaSolution.com.networking.state

import ghoudan.anfaSolution.com.app_models.UserCredentials

sealed class UserCredentialsState {
    data class Success(val data: UserCredentials?) : UserCredentialsState()
    data class Code(val code: Int?) : UserCredentialsState()
    data class Error(val exception: Throwable) : UserCredentialsState()
    object Loading : UserCredentialsState()
}
