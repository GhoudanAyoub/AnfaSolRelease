package ghoudan.anfaSolution.com.app_models

import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val orderSlotId: Int?,
    val name: String?)
