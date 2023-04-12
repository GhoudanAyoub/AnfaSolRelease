@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.networking.entity

import ghoudan.anfaSolution.com.networking.serializer.DateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*
import kotlinx.serialization.SerialName

@Serializable
data class UserEntity(
    @SerialName("User_Name")
    val username: String? = null,
    @SerialName("PassMobileApp")
    val password: String? = null,
    @SerialName("User_Security_ID")
    val userId: String? = null
)
