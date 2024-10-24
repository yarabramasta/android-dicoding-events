package dev.ybrmst.dicodingevents.domain.models

import dev.ybrmst.dicodingevents.data.local.FavoriteEventEntity
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class EventPreview(
  val id: Int,
  val name: String,
  val summary: String,
  val cityName: String,
  val imageLogo: String,
  val isFavorite: Boolean = false,
) {
  companion object {
    fun fake() = EventPreview(
      id = Random.nextInt(),
      name = "[Offline] IDCamp Connect  Roadshow - Solo",
      summary = "IDCamp Connect Roadshow 2024 akan dilaksanakan pada hari Jumat, 18 Oktober 2024 pukul 13.00 - 17.00 WIB",
      cityName = "Kota Surakarta",
      imageLogo = "https://placehold.co/400.png"
    )
  }
}

fun EventPreview.toEntity(): FavoriteEventEntity {
  return FavoriteEventEntity(
    eventId = id,
    name = name,
    summary = summary,
    cityName = cityName,
    imageLogo = imageLogo,
  )
}