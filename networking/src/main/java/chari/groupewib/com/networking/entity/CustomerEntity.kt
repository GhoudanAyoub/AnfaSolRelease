@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.networking.entity

import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.serializer.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CustomerEntity(
    val active: Boolean? = null,
    val customerId: Int,
    val channelId: Int? = null,
    val cin: String? = null,
    val code: String? = null,
    val contactName: String? = null,
    val ice: String? = null,
    val `if`: String? = null,
    val name: String? = null,
    val paymentModeId: Int? = null,
    val phoneNumber: String? = null,
    val rc: String? = null,
    val taxable: Boolean? = null,
    val userId: Int? = null,
    val walletAmount: Double? = null,
    val walletEnabled: Boolean? = null,
    val longitudeOfCreation: String? = null,
    val latitudeOfCreation: String? = null,
    val user: User? = null,
    val customerStatusId: Int? = null,
)
