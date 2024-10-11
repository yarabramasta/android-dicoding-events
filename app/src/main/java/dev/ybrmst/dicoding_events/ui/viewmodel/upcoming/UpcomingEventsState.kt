package dev.ybrmst.dicoding_events.ui.viewmodel.upcoming

import dev.ybrmst.dicoding_events.domain.EventPreview

data class UpcomingEventsState(
  val events: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
  val isSearching: Boolean = false,
  val searchQuery: String = "",
) {
  companion object {
    val Initial = UpcomingEventsState(emptyList())

    val Fetching = UpcomingEventsState(emptyList())

    fun loaded(events: List<EventPreview>) = UpcomingEventsState(
      events,
      isFetching = false,
      isError = false
    )

    fun search(query: String) = UpcomingEventsState(
      emptyList(),
      searchQuery = query,
      isFetching = false,
      isSearching = true
    )

    val Error = UpcomingEventsState(
      emptyList(),
      isFetching = false,
      isError = true
    )
  }
}