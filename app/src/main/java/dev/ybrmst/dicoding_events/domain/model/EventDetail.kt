package dev.ybrmst.dicoding_events.domain.model

data class EventDetail(
  val id: Int,
  val name: String,
  val summary: String,
  val description: String,
  val imageLogo: String,
  val mediaCover: String,
  val category: String,
  val ownerName: String,
  val cityName: String,
  val quota: Int,
  val registrants: Int,
  val beginTime: String,
  val endTime: String,
  val link: String,
) {
  companion object {
    val Empty = EventDetail(
      id = 0,
      name = "",
      summary = "",
      description = "",
      imageLogo = "",
      mediaCover = "",
      category = "",
      ownerName = "",
      cityName = "",
      quota = 0,
      registrants = 0,
      beginTime = "2024-10-18 13:00:00",
      endTime = "2024-10-18 13:00:00",
      link = "",
    )
  }
}