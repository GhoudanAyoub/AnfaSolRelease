@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.app_models

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class OrderLine(
    val orderLineId: Int? = null,
    val orderId: Int? = null,
    var articleId: Int,
    val unitPrice: Double? = null,
    var quantity: Int,
    val totalAmountHT: Double? = null,
    val totalAmountTTC: Double? = null,
    val discountPercent: Double? = null,
    val tvaPercent: Double? = null,
    val tvaAmount: Double? = null,
    val promotionsApplied: String? = null,
    val discountedUnitPrice: Double? = null,
    val discountedUnitPriceTTC: Double? = null,
    val discountAmount: Double? = null,
    val typeAction: Int? = null,
    val errorId: Int? = null,
    val errorDescription: String? = null,
    val temporaryId: Int? = null,
    val discountAmountTtc: Double? = null,
    val unitCode: String? = null,
    val discountedSalesUnitPriceTTC: Double? = null,
    val salesUnitPriceTTC: Double? = null,
    var isGift: Boolean? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderLine

        if (articleId != other.articleId) return false

        return true
    }

    override fun hashCode(): Int {
        return articleId ?: 0
    }
}
