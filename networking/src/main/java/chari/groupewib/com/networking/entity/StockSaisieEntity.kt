package chari.groupewib.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockSaisieEntity(
    @SerialName("No")
    val no: String,
    @SerialName("Description")
    val description: String,
    @SerialName("Stock_Colis")
    val resteColis: String,
    @SerialName("Stock_Poids")
    val restePoids: String,
    @SerialName("ColisAch")
    val colisAchatPL: String,
    @SerialName("ColisRetourFour")
    val qteColisRF: String
)
