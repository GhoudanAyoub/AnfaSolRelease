package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PurchaseOrderHeaderRequest(
    @SerialName("Pay_to_Vendor_No")
    val supplierId:  String? = null,
    @SerialName("Buy_from_Vendor_No")
    val Buy_from_Vendor_No:  String? = null,)
