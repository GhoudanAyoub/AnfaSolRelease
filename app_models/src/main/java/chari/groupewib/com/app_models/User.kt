@file:UseSerializers(DateSerializer::class)
package ghoudan.anfaSolution.com.app_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*
import kotlinx.serialization.SerialName

@Serializable
@Entity
data class User(
    @PrimaryKey
    val userId: String,
    val active: Boolean? = false,
    val username: String? = null,
    val password: String? = null
)
