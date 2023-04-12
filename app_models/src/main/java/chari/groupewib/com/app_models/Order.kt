@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.app_models

import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class Order(
    val orderId: Int? = null,
    val code: String? = null,
    val customerId: Int? = null,
    val totalAmountHT: Double? = null,
    val totalAmountTTC: Double? = null,
    val totalDiscount: Double? = null,
    val totalDiscountedAmountHT: Double? = null,
    val totalTimbre: Double? = null,
    val totalTva: Double? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    var desiredDeliveryDate: Date? = null,
    val deliveryDate: Date? = null,
    val synchronizationDate: Date? = null,
    var orderStatusId: Int? = null,
    val promotionsApplied: String? = null,
    val addToErp: Boolean? = null,
    var paymentModeId: Int? = null,
    val totalEscompte: Double? = null,
    val confirmedAt: Date? = null
) {
    companion object {
        fun generateEmptyOrder(customerId: Int): Order {
            return Order(
                orderId = 0,
                orderStatusId = 1,
                customerId = customerId
            )
        }
    }
}
