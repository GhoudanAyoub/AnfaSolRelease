package chari.groupewib.com.networking.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackingListEntity(
    @SerialName("@odata.etag")
    val etag: String,
    @SerialName("Document_No")
    val document_No: String,
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
    @SerialName("indice")
    val indice: Int,
    @SerialName("Ligne_ach")
    val Ligne_ach: Int,
    @SerialName("No_Art")
    val No_Art: String,
    var isChecked: Boolean = false
)
