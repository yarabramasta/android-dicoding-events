package dev.ybrmst.dicoding_events.domain.errors

data class FetchEventDetailError(
  override val message: String = "Unable to retrieve event information.",
) : Throwable(message)