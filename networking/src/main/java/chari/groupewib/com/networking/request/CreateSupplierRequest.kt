package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CreateSupplierRequest(
    @SerialName("No")
    val code: String? = null,
    @SerialName("Name")
    val name: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    @SerialName("Country_Region_Code")
    val region: String? = null)
