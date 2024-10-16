package dev.ybrmst.dicoding_events.domain.error

data class FetchEventDetailNetworkError(
  override val message: String? = "Failed to fetch event information. Please check your internet connection.",
) : Throwable(message)