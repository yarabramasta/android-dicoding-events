package dev.ybrmst.dicoding_events.domain.errors

enum class ErrorCodes {
  NETWORK_ERROR,
  UNKNOWN_ERROR,
  BAD_REQUEST,
}

open class BaseError(
  override val message: String,
  val code: ErrorCodes = ErrorCodes.UNKNOWN_ERROR,
) : Throwable(message) {

  override fun toString(): String {
    return "BaseError(code=$code, message=$message, cause=$cause)"
  }
}

fun BaseError.fromThrowable(throwable: Throwable): BaseError {
  return BaseError(
    throwable.message ?: "Uh oh! An error occurred.",
    ErrorCodes.UNKNOWN_ERROR
  )
}