package dev.ybrmst.dicoding_events.domain.models

sealed class Result<out D, out E> {
  data class Success<D>(val data: D) : Result<D, Nothing>()
  data class Error<E>(val error: E) : Result<Nothing, E>()
  data object Loading : Result<Nothing, Nothing>()

  fun isSuccess(): Boolean = this is Success
  fun hasError(): Boolean = this is Error
  fun isLoading(): Boolean = this is Loading

  fun getOrNull(): D? = (this as? Success)?.data
  fun errorOrNull(): E? = (this as? Error)?.error

  override fun toString(): String {
    return when (this) {
      is Success -> "Success[data=$data]"
      is Error -> "Error[error=$error]"
      Loading -> "Loading"
    }
  }
}