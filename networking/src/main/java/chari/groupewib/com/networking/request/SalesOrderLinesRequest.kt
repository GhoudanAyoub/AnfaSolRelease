package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SalesOrderLinesRequest(
    @SerialName("Document_Type")
    val doc_type: String?,
    @SerialName("Document_No")
    val doc_No: String?,
    val Type: String?,
    val No: String?,
    val Parcel: Int? = 4,
    @SerialName("Requested_Total_Weight")
    val total_weight: Double? = 0.0,
    @SerialName("Requested_Total_Units")
    val total_Unit: Double? = 0.0,
)

@kotlinx.serialization.Serializable
data class PurchaseOrderLinesRequest(
    @SerialName("Document_Type")
    val doc_type: String? ="Order",
    @SerialName("Document_No")
    val doc_No: String?,
    val No: String?,
    val Line_No: String?,
    val Parcel: Int? = 4,
    @SerialName("Total_Weight")
    val total_weight: Double? = 0.0,
    @SerialName("Total_Units")
    val total_Unit: Double? = 0.0,
)
