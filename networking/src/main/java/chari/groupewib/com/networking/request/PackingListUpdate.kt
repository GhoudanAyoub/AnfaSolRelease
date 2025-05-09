package chari.groupewib.com.networking.request

import kotlinx.serialization.Serializable

@Serializable
data class PackingListUpdate(
    val Saisie_colis : Int = 1,
)
