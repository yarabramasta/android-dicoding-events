package dev.ybrmst.dicoding_events.domain.errors

data class FetchEventsNetworkError(
  override val message: String? = "Failed to fetch events. Please check your internet connection.",
) : Throwable(message)