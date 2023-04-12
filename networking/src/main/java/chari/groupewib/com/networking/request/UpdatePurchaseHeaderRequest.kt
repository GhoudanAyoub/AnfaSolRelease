package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class UpdatePurchaseHeaderRequest(
    @SerialName("Buy_from_Vendor_No")
    val supplierId:  String? = null,
    @SerialName("Buy_from_Vendor_Name")
    val supplierName:  String? = null,
    @SerialName("Buy_from_Contact_No")
    val Phone_no:  String? = null,
    @SerialName("Vendor_Invoice_No")
    val facture_no:  String? = null,
    @SerialName("Vendor_Shipment_No")
    val Vendor_Invoice_No:  String? = null,
    @SerialName("Order_Date")
    val orderDate: String? = null,
    @SerialName("Requested_Receipt_Date")
    val requestedRecievedDate: String? = null,
    @SerialName("Expected_Receipt_Date")
    val ExpectedRecievedDate: String? = null,
    @SerialName("Promised_Receipt_Date")
    val promisReceivedDate: String? = null)
