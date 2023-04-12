package ghoudan.anfaSolution.com.app_models

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object DateSerializer : KSerializer<Date> {
    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(dateFormat.format(value))

    override fun deserialize(decoder: Decoder): Date =
        requireNotNull(dateFormat.parse(decoder.decodeString()))
}
