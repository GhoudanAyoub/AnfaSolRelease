package ghoudan.anfaSolution.com.app_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class PurchaseOrder(
    @SerialName("@odata.etag")
    val etag: String?,
    @SerialName("Document_Type")
    val doc_type: String?,
    @SerialName("Buy_from_Vendor_No")
    val customerId: String? = null,
    @SerialName("Buy_from_Vendor_Name")
    val supplierName: String? = null,
    @SerialName("No")
    val counter: String? = null,
    @SerialName("Posting_Description")
    val description: String? = null,
    @SerialName("Order_Date")
    val desiredDeliveryDate: String? = null,
    @SerialName("Due_Date")
    val Due_Date: String? = null,
    @SerialName("Requested_Receipt_Date")
    val requestReceiptDate: String? = null,
    @SerialName("Expected_Receipt_Date")
    val Expected_Receipt_Date: String? = null,
    @SerialName("Promised_Receipt_Date")
    val Promised_Receipt_Date: String? = null,
    @SerialName("Vendor_Invoice_No")
    val expSupplier:  String? = null,
    @SerialName("Pay_to_Vendor_No")
    val Pay_to_Vendor_No: String? = null,
    @SerialName("Pay_to_Name")
    val Pay_to_Name: String? = null,
    @SerialName("Pay_to_Contact")
    val Pay_to_Contact: String? = null,
) : Parcelable
