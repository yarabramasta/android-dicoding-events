package dev.ybrmst.dicoding_events.ui.viewmodel.event.finished

import dev.ybrmst.dicoding_events.domain.EventPreview

data class FinishedEventsUiState(
  val events: List<EventPreview> = emptyList(),
  val isFetching: Boolean = false,
  val isError: Boolean = false,
  val isRefreshing: Boolean = false,
  val query: String = "",
) {
  companion object {
    private val Empty = FinishedEventsUiState(
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

    fun loaded(events: List<EventPreview>) = FinishedEventsUiState(
      events = events,
      isFetching = false,
      isError = false,
      isRefreshing = false,
    )
  }
}