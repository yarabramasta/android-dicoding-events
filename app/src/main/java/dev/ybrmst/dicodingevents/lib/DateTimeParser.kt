package dev.ybrmst.dicodingevents.lib

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DateTimeParser : KSerializer<LocalDateTime> {
  @OptIn(FormatStringsInDatetimeFormats::class)
  private val formatter = LocalDateTime.Format {
    byUnicodePattern("yyyy-MM-dd HH:mm:ss")
  }

  override val descriptor = String.serializer().descriptor

  override fun serialize(
    encoder: Encoder,
    value: LocalDateTime,
  ) = encoder.encodeString(formatter.format(value))

  override fun deserialize(
    decoder: Decoder,
  ): LocalDateTime = formatter.parse(decoder.decodeString())
}