package chari.groupewib.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemEntity(
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("No")
    val code: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Base_Unit_of_Measure")
    val unitCode: String? = null,
    @SerialName("Unit_Price")
    val unitCost: Double? = 0.0,
    @SerialName("Line_No")
    val Line_No: Int? = 0,
    var Item_Category_Code:  String? = null,
    val Blocked: Boolean? = false
)
