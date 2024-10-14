package dev.ybrmst.dicoding_events.ui.viewmodel.event.detail

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicoding_events.domain.EventDetail

@Immutable
data class DetailState(
  val event: EventDetail? = null,
  val isFetching: Boolean = false,
  val isRefreshing: Boolean = false,
  val isError: Boolean = false,
) {
  companion object {
    private val Empty = DetailState(
      event = null,
      isFetching = false,
      isRefreshing = false,
      isError = false
    )

    val Initial = Empty.copy(isFetching = true)

    val Fetching = Empty.copy(isFetching = true)

    val Refreshing = Empty.copy(isRefreshing = true)

    val Error = Empty.copy(isError = true)

    fun loaded(event: EventDetail) = DetailState(
      event = event,
      isFetching = false,
      isRefreshing = false,
      isError = false
    )
  }
}