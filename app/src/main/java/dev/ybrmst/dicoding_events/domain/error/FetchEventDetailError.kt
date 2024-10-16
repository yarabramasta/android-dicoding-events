package dev.ybrmst.dicoding_events.domain.error

data class FetchEventDetailError(
  override val message: String = "Unable to retrieve event information.",
) : Throwable(message)