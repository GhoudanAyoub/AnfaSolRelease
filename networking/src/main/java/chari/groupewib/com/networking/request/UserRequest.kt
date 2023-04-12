package ghoudan.anfaSolution.com.networking.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val firstName: String?,
    val lastName: String?
)
