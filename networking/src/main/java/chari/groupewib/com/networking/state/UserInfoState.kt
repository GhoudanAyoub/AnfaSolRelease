package ghoudan.anfaSolution.com.networking.state

import ghoudan.anfaSolution.com.app_models.User

sealed class UserInfoState {
    data class Success(val data: User) : UserInfoState()
    data class Error(val exception: Exception) : UserInfoState()
    object Loading : UserInfoState()
}
