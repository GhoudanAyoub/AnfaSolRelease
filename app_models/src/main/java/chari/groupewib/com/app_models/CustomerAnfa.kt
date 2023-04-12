package ghoudan.anfaSolution.com.app_models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
@Entity
data class CustomerAnfa(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @SerialName("No")
    val code: String,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Enseigne")
    val description: String? = null,
    val Phone_No: String? = null,
    val City: String? = null,
    val Post_Code: String? = null,
    val Address: String? = null,
    val Sector: String? = null,
    @SerialName("Shipping_Agent_Code")
    val agent_code: String? = null,
    val Blocked: String? = null,
    @SerialName("@odata.etag")
    val etag: String? = null
) : Parcelable
