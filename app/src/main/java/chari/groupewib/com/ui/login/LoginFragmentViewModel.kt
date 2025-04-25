package  ghoudan.anfaSolution.com.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ghoudan.anfaSolution.com.networking.SingleLiveEvent
import ghoudan.anfaSolution.com.networking.repository.LoginRepository
import ghoudan.anfaSolution.com.networking.repository.UserInfoRepository
import ghoudan.anfaSolution.com.networking.state.UserCredentialsState
import ghoudan.anfaSolution.com.utils.Constants
import ghoudan.anfaSolution.com.utils.PreferencesModule.CURRENT_CREDENTIALS_PREFERENCES_NAME
import ghoudan.anfaSolution.com.utils.PreferencesModule.CURRENT_USER_PREFERENCES_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.offline.Resource
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository,
    @Named(CURRENT_CREDENTIALS_PREFERENCES_NAME)
    private val userDataPreferences: SharedPreferences,
    @Named(CURRENT_USER_PREFERENCES_NAME)
    private val currentUserPreferences: SharedPreferences,
) : ViewModel() {


    private val loginResultLiveData: MutableLiveData<UserCredentialsState> = MutableLiveData()
    val userLoginResult: LiveData<UserCredentialsState> = loginResultLiveData

    private val userInfoLiveData: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val userInfo: LiveData<Resource<List<User>>> = userInfoLiveData

    private val sendSmsCodeResult: SingleLiveEvent<UserCredentialsState> = SingleLiveEvent()
    val sendSmsCodeLiveData: LiveData<UserCredentialsState> = sendSmsCodeResult


    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(phoneNumber, password).collect { userLoginResult ->
                if (userLoginResult is UserCredentialsState.Success) {
                    val userCredentialsJson = Json.encodeToString(userLoginResult.data)
                    userDataPreferences.edit()
                        .putString(Constants.USER_CREDENTIALS_KEY, userCredentialsJson).apply()
                } else {
                    loginResultLiveData.value = userLoginResult
                }
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            userInfoRepository.fetchUserInfo().collect { userInfoState ->
                userInfoLiveData.value = userInfoState
            }
        }
    }

    fun sendCode(phoneNumber: String) {
        viewModelScope.launch {
            loginRepository.sendSmsCode(phoneNumber)
                .collect { result ->
                    sendSmsCodeResult.value = result
                }
        }
    }
}
