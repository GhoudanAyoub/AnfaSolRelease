package ghoudan.anfaSolution.com.networking.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EPApiCollectionResponse<T>(
    @SerialName("value")
    val data: T,
)
