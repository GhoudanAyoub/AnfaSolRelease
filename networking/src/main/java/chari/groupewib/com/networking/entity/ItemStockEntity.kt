package chari.groupewib.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ItemStockEntity(
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("Lot_No")
    val code: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Entry_Type")
    val type: String? = null,
    @SerialName("Unit_of_Measure_Code")
    val unitCode: String? = null,
    @SerialName("Quantity")
    val quantity: Double? = 0.0,
    @SerialName("Item_Family")
    val family: String? =null,
    @SerialName("Item_No")
    val Item_No: String? =null,
    @SerialName("Document_No")
    val Document_No: String? =null
)
