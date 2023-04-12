package chari.groupewib.com.networking.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CreateClientRequest(
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Enseigne")
    val description: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    val Address: String? = null,
    val Sector: String? = null,
    val Blocked: String? = null)
