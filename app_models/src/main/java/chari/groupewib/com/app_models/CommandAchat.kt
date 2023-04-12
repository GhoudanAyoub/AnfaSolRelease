@file:UseSerializers(DateSerializer::class)

package ghoudan.anfaSolution.com.app_models

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers


@Serializable
@Parcelize
data class CommandAchat(

    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("No")
    val No:  String? = null,
    @SerialName("Sell_to_Customer_No")
    val customerId:  String? = null,
    @SerialName("Sell_to_Customer_Name")
    val customerName:  String? = null,
    @SerialName("Document_Type")
    val doc_Type: String? = null,
    @SerialName("Salesperson_Code")
    val salerCode: String? = null,
    @SerialName("COUNT")
    val counter: Int? = null,
    @SerialName("Posting_Description")
    val description: String? = null,
    @SerialName("Shipment_Date")
    val desiredDeliveryDate: String? = null,
    @SerialName("Due_Date")
    val Due_Date: String? = null,
    @SerialName("Posting_Date")
    val Posting_Date: String? = null,
    @SerialName("Status")
    val Status: String? = null,
    @SerialName("Amount_Including_VAT")
    val totalAmountTTC: Double? = null
) : Parcelable
