package dev.ybrmst.dicoding_events.domain.models

data class FavoriteEvent(
  val id: Int,
  val name: String,
  val summary: String,
  val cityName: String,
  val imageLogo: String,
)