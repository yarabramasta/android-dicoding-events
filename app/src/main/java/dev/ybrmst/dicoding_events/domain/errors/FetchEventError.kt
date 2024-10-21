package dev.ybrmst.dicoding_events.domain.errors

sealed class FetchEventError(code: ErrorCodes, message: String) :
  BaseError(message = message, code = code) {

  class BadRequest(message: String) : FetchEventError(
    code = ErrorCodes.BAD_REQUEST,
    message = message,
  )

  class NetworkError(message: String) : FetchEventError(
    code = ErrorCodes.NETWORK_ERROR,
    message = message,
  )

  class UnknownError(message: String) : FetchEventError(
    code = ErrorCodes.UNKNOWN_ERROR,
    message = message,
  )

  override fun toString(): String {
    return "FetchEventError(code=$code, message=$message)"
  }
}