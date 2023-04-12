package ghoudan.anfaSolution.com.networking.entity

import kotlinx.serialization.Serializable

@Serializable
data class SlotEntity(
    val orderSlotId: Int?,
    val name: String?
    )
