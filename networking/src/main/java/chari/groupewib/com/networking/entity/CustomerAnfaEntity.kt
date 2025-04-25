package ghoudan.anfaSolution.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerAnfaEntity(

    @SerialName("No")
    val code: String? = null,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Enseigne")
    val description: String? = null,
    @SerialName("@odata.etag")
    val etag: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    val Address: String? = null,
    val Sector: String? = null,
    @SerialName("Shipping_Agent_Code")
    val agent_code: String? = null,
    @SerialName("Blocked")
    val Blocked: String? = null
)
