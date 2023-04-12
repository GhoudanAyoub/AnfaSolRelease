package chari.groupewib.com.app_models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
@Entity
data class Item(
    @PrimaryKey
    val code: String,
    val description: String? = null,
    val unitCode: String? = null,
    val unitCost: Double? = 0.0,
    @SerialName("Requested_Total_Units")
    var itemUnits: Double? = 0.0,
    @SerialName("Requested_Total_Weight")
    var itemWeight: Double? = 0.0,
    @SerialName("Parcel")
    var parcel: Int? = 0,
    var Line_No: Int? = 0,
    var Document_Type: String? = null,
    var Document_No: String? = null,
    var etag: String? = null,
    var hideUnits : Boolean = false,
    var quantity: Double? = 0.0,
    var Item_Category_Code: String? =null,
    var Type: String? =null,
    var Family: String? =null,
    var Blocked: Boolean? = false
):Parcelable

@Serializable
data class SingleItemResponse(
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("No")
    val code: String? = null,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Base_Unit_of_Measure")
    val unitCode: String? = null,
    @SerialName("Unit_Price")
    val unitCost: Double? = 0.0,
    var Item_Category_Code: String? =null,
    var Type: String? =null,
    var Family: String? =null,
    var Blocked: Boolean? = false
)
