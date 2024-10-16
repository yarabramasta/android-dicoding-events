package dev.ybrmst.dicoding_events.domain.error

data class FetchEventDetailNotFoundError(
  override val message: String = "Event information could not be found.",
) : Throwable(message)