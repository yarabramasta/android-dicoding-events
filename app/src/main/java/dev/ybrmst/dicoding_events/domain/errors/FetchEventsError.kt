package dev.ybrmst.dicoding_events.domain.errors

data class FetchEventsError(
  override val message: String? = "Unable to retrieve events.",
) : Throwable(message)