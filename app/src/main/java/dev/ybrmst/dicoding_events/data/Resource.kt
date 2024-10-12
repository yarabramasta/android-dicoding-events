package dev.ybrmst.dicoding_events.data

sealed class Resource<T>(
  val data: T? = null,
  val message: String? = null,
) {
  class Success<T>(data: T) : Resource<T>(data)
  class Error<T>(ex: Throwable, data: T? = null) : Resource<T>(data, ex.message)
}