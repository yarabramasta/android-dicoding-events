package dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming

import dev.ybrmst.dicoding_events.domain.EventPreview

data class UpcomingEventsUiState(
  val events: List<EventPreview> = emptyList(),
  val isFetching: Boolean = false,
  val isError: Boolean = false,
  val isRefreshing: Boolean = false,
  val query: String = "",
) {
  companion object {
    private val Empty = UpcomingEventsUiState(
      events = emptyList(),
      isFetching = false,
      isError = false,
      isRefreshing = false,
      query = ""
    )

    val Initial = Empty.copy(isFetching = true)

    val Fetching = Empty.copy(isFetching = true)

    val Refreshing = Empty.copy(isRefreshing = true)

    val Error = Empty.copy(isError = true)

    fun loaded(events: List<EventPreview>) = UpcomingEventsUiState(
      events = events,
      isFetching = false,
      isError = false,
      isRefreshing = false,
    )
  }
}