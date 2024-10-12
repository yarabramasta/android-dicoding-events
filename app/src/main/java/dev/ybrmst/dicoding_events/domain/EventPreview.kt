package dev.ybrmst.dicoding_events.domain

data class EventPreview(
  val id: Int,
  val name: String,
  val summary: String,
  val cityName: String,
  val imageLogo: String,
)