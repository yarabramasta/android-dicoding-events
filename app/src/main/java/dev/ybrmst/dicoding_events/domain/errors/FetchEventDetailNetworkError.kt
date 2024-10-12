package dev.ybrmst.dicoding_events.domain.errors

data class FetchEventDetailNetworkError(
  override val message: String? = "Failed to fetch event information. Please check your internet connection.",
) : Throwable(message)