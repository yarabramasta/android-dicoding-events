package dev.ybrmst.dicoding_events.domain.model

data class EventPreview(
  val id: Int,
  val name: String,
  val summary: String,
  val cityName: String,
  val imageLogo: String,
)