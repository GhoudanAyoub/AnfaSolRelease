package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemEntityResponse(
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("No")
    val code: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Unit_of_Measure_Code")
    val unitCode: String? = null,
    @SerialName("Unit_Price")
    val unitCost: Double? = 0.0,
    @SerialName("Line_No")
    val Line_No: Int? = 0,
    var Item_Category_Code: String? = null,
    var Type: String? = null,
    var Family: String? = null,
    var Blocked: Boolean? = false,
    @SerialName("Requested_Total_Units")
    var itemUnits: Double? = 0.0,
    @SerialName("Requested_Total_Weight")
    var itemWeight: Double? = 0.0,
    @SerialName("Parcel")
    var parcel: Int? = 0
)

@Serializable
data class PurchaseItemEntityResponse(
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("No")
    val code: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Unit_of_Measure_Code")
    val unitCode: String? = null,
    @SerialName("Unit_Price")
    val unitCost: Double? = 0.0,
    @SerialName("Line_No")
    val Line_No: Int? = 0,
    var Item_Category_Code: String? = null,
    var Type: String? = null,
    var Family: String? = null,
    var Blocked: Boolean? = false,
    @SerialName("Total_Units")
    var itemUnits: Double? = 0.0,
    @SerialName("Total_Weight")
    var itemWeight: Double? = 0.0,
    @SerialName("Parcel")
    var parcel: Int? = 0
)
