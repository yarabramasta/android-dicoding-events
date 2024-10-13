package dev.ybrmst.dicoding_events.ui.viewmodel.home

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicoding_events.domain.EventPreview

@Immutable
data class HomeState(
  val upcomingEvents: List<EventPreview>,
  val pastEvents: List<EventPreview>,
  val isFetching: Boolean = true,
  val isRefreshing: Boolean = false,
  val isError: Boolean = false,
) {

  companion object {
    private val Empty = HomeState(
      upcomingEvents = emptyList(),
      pastEvents = emptyList(),
      isFetching = true,
      isRefreshing = false,
      isError = false
    )

    val Initial = Empty

    val Fetching = Empty.copy(isFetching = true)

    val Refreshing = Empty.copy(isRefreshing = true)

    val Error = Empty.copy(isError = true)

    fun loaded(
      highlights: List<EventPreview>,
      events: List<EventPreview>,
    ) =
      HomeState(
        upcomingEvents = highlights,
        pastEvents = events,
        isFetching = false,
        isRefreshing = false,
        isError = false,
      )
  }
}