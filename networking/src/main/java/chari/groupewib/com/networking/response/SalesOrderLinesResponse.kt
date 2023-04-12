package chari.groupewib.com.networking.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SalesOrderLinesResponse(
    @SerialName("@odata.etag")
    val etag: String?,
    @SerialName("Line_No")
    val Line_No: Int?,
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
    @SerialName("Requested_Quantity")
    val quantity: Double? = 0.0,
)

@kotlinx.serialization.Serializable
data class ErrorEntity(
    val error: ErrorData?,
)
@kotlinx.serialization.Serializable
data class ErrorData(
    val code: String?,
    val message: String?
)
