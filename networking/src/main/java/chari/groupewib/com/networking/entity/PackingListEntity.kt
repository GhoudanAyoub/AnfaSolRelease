package chari.groupewib.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackingListEntity(
    @SerialName("Date_Ach")
    val dateAch: String,
    @SerialName("Pu_ach")
    val puAch: String,
    @SerialName("Code_four")
    val codeFour: String,
    @SerialName("Num_Colis")
    val numColis: String,
    @SerialName("Reste_colis")
    val resteColis: String,
    @SerialName("Reste_poids")
    val restePoids: String,
    @SerialName("Saisie_colis")
    val saisieColis: String,
    var isChecked: Boolean = false
)
