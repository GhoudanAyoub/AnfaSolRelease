@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.app_models

import java.util.Date
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers


@Serializable
data class Command(
    val orderId: Int,
    val customerId: Int? = null,
    val code: String? = null,
    val customerFullName: String? = null,
    val customerPhoneNumber: String? = null,
    val confirmedAt: Date?,
    val totalAmountTTC: Double? = null,
    val totalQuantity: Int? = null,
    val totalLines: Int? = null,
    val preparationStartAt: Date? = null,
    val preparationEndAt: Date? = null,
    val deliveryModeId: Int? = null,
    var preparationStatus: Int = 1,
    val desiredDeliveryDate: Date? = null,
    val counter: String? = null
)
