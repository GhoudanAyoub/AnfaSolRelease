package ghoudan.anfaSolution.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupplierAnfaEntity(
    @SerialName("No")
    val code: String? = null,
    @SerialName("Name")
    val name: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    @SerialName("Country_Region_Code")
    val region: String? = null,
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("Vendor_Posting_Group")
    val vpg: String? = null,
)
