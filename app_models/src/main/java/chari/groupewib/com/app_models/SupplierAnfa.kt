package ghoudan.anfaSolution.com.app_models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
@Entity
data class SupplierAnfa(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerialName("No")
    val code: String? = null,
    @SerialName("Name")
    val name: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    @SerialName("Country_Region_Code")
    val region: String? = null,
    @SerialName("@odata.etag")
    val etag: String? = null,
    @SerialName("Vendor_Posting_Group")
val vpg: String? = null
):Parcelable
