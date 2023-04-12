package ghoudan.anfaSolution.com.networking.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCollection<T>(
    @SerialName("collection")
    val collectionData: T,
    val count: Int
)
