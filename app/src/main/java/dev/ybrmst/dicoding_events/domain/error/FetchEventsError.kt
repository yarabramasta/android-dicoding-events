package dev.ybrmst.dicoding_events.domain.error

data class FetchEventsError(
  override val message: String? = "Unable to retrieve events.",
) : Throwable(message)