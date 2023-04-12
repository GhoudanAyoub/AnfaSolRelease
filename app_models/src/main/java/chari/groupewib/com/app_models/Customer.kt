@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.app_models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers


@Serializable
data class Customer(
    val active: Boolean? = null,
    val customerId: Int,
    val addToErp: Boolean? = null,
    val channelId: Int? = null,
    val cin: String? = null,
    val code: String? = null,
    val contactName: String? = null,
    val ice: String? = null,
    val `if`: String? = null,
    val name: String? = null,
    val patente: String? = null,
    val paymentModeId: Int? = null,
    val phoneNumber: String? = null,
    val rc: String? = null,
    val taxable: Boolean? = null,
    val user: User? = null,
    val userId: Int? = null,
    val walletAmount: Double? = null,
    var walletEnabled: Boolean? = null,
    val longitudeOfCreation: String? = null,
    val latitudeOfCreation: String? = null,
    val customerStatusId: Int? = null,
)
