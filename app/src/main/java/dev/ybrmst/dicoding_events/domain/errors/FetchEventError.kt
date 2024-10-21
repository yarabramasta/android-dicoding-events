package dev.ybrmst.dicoding_events.domain.errors

sealed class FetchEventError : Throwable() {
  data class InvalidResponse(override val message: String) : FetchEventError()

  data class NetworkError(override val message: String) : FetchEventError()

  data class Unknown(override val message: String) : FetchEventError()

  companion object {
    fun fromThrowable(throwable: Throwable): FetchEventError {
      return when (throwable) {
        is FetchEventError -> throwable
        else -> Unknown(throwable.message ?: "Unknown error")
      }
    }
  }
}