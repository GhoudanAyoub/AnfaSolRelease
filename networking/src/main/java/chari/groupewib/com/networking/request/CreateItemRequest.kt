package chari.groupewib.com.networking.request

import kotlin.random.Random

@kotlinx.serialization.Serializable
data class CreateItemRequest(
    val Description: String? = null,
    val Base_Unit_of_Measure: String? = null,
    var Item_Category_Code: String? =null,
    var Type: String? =null,
    var Family: String? =null,
    var Blocked: Boolean? = false,
    val No: String = Random(100000).nextInt().toString())
