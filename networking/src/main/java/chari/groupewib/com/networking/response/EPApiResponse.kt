package ghoudan.anfaSolution.com.networking.response

import kotlinx.serialization.Serializable

@Serializable
data class EPApiResponse<T>(
    val message: String?,
    val exception: String?,
    val data: T
)
