package dev.ybrmst.dicoding_events.domain.errors

data class FetchEventDetailNotFoundError(
  override val message: String = "Event information could not be found.",
) : Throwable(message)