package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSalesHeaderRequest (
    @SerialName("Sell_to_Customer_No")
    val customerId:  String? = null,
    @SerialName("Sell_to_Address")
    val addressPostal:  String? = null,
    @SerialName("Sell_toCity2")
    val city:  String? = null,
    @SerialName("Due_Date")
    val Due_Date:  String? = null,
    @SerialName("Posting_Date")
    val Posting_Date:  String? = null,
    @SerialName("Sell_to_Phone_No")
    val Sell_to_Phone_No:  String? = null,
    @SerialName("Salesperson_Code")
    val Salesperson_Code:  String? = null,
    @SerialName("Order_Date")
    val Order_Date:  String? = null
)
