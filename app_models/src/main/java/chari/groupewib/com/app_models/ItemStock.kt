package chari.groupewib.com.app_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ItemStock(
    val etag: String? = null,
    val code: String? = null,
    val description: String? = null,
    val type: String? = null,
    val unitCode: String? = null,
    val quantity: Double? = 0.0,
    val family: String? =null,
    val Item_No: String? =null,
    val Document_No: String? =null
)
