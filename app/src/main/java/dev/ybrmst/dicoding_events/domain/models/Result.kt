package dev.ybrmst.dicoding_events.domain.models

sealed class Result<out D, out E> {
  data class Success<D>(val data: D) : Result<D, Nothing>()
  data class Error<E>(val error: E) : Result<Nothing, E>()
  data object Loading : Result<Nothing, Nothing>()

  fun isSuccess(): Boolean = this is Success
  fun isError(): Boolean = this is Error
  fun isLoading(): Boolean = this is Loading

  fun getOrNull(): D? = (this as? Success)?.data
  fun errorOrNull(): E? = (this as? Error)?.error

  fun whenOrElse(
    onSuccess: (D) -> Unit = {},
    onError: (E) -> Unit = {},
    onLoading: () -> Unit = {},
  ) {
    when (this) {
      is Success -> onSuccess(data)
      is Error -> onError(error)
      Loading -> onLoading()
    }
  }

  override fun toString(): String {
    return when (this) {
      is Success -> "Success[data=$data]"
      is Error -> "Error[error=$error]"
      Loading -> "Loading"
    }
  }
}