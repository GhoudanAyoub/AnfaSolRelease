package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SalesOrderHeaderRequest(
    @SerialName("Sell_to_Customer_No")
    val customerId: String? = null
)
