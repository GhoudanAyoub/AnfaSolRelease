package ghoudan.anfaSolution.com.networking.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val phoneNumber: String,
    val password: String? = null,
    val code: String? = null,
    val user: UserRequest? = null,
    @SerialName("SkipSendCode") var skipSendCode: Boolean? = false
)
