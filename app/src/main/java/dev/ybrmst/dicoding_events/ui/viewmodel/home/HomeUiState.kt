package dev.ybrmst.dicoding_events.ui.viewmodel.home

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicoding_events.domain.model.EventPreview

@Immutable
data class HomeUiState(
  val upcomingEvents: List<EventPreview>,
  val pastEvents: List<EventPreview>,
  val isFetching: Boolean = false,
  val isRefreshing: Boolean = false,
  val isError: Boolean = false,
) {

  companion object {
    private val Empty = HomeUiState(
      upcomingEvents = emptyList(),
      pastEvents = emptyList(),
      isFetching = false,
      isRefreshing = false,
      isError = false
    )

    val Initial = Empty.copy(isFetching = true)

    val Fetching = Empty.copy(isFetching = true)

    val Refreshing = Empty.copy(isRefreshing = true)

    val Error = Empty.copy(isError = true)

    fun loaded(
      highlights: List<EventPreview>,
      events: List<EventPreview>,
    ) =
      HomeUiState(
        upcomingEvents = highlights,
        pastEvents = events,
        isFetching = false,
        isRefreshing = false,
        isError = false,
      )
  }
}