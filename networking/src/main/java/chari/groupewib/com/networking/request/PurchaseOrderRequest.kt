package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PurchaseOrderRequest(
    @SerialName("buyFromVendorNumber")
    val supplierId:  String? = null,
    @SerialName("buyFromVendorName")
    val supplierName:  String? = null,
    @SerialName("payToContact")
    val Phone_no:  String? = null,
    @SerialName("invoiceDiscCode")
    val facture_no:  String? = null,
    @SerialName("vendorInvoiceNumber")
    val expSupplier:  String? = null,
    @SerialName("orderDate")
    val orderDate: String? = null,
    @SerialName("requestedReceiptDate")
    val requestedRecievedDate: String? = null,
    @SerialName("expectedReceiptDate")
    val ExpectedRecievedDate: String? = null,
    @SerialName("promisedReceiptDate")
    val promisReceivedDate: String? = null)
