package dev.ybrmst.dicoding_events.domain.error

data class FatalError(
  override val message: String = "An unexpected error occurred! Please try again later.",
) : Throwable(message)