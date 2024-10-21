package dev.ybrmst.dicoding_events.domain.models

import kotlin.random.Random

data class EventPreview(
  val id: Int,
  val name: String,
  val summary: String,
  val cityName: String,
  val imageLogo: String,
) {
  companion object {
    fun fake() = EventPreview(
      id = Random.nextInt(),
      name = "Event Name",
      summary = "Event Summary",
      cityName = "City Name",
      imageLogo = "https://placehold.co/400.png"
    )
  }
}