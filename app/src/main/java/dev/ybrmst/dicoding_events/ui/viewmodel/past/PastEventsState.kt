package dev.ybrmst.dicoding_events.ui.viewmodel.past

import dev.ybrmst.dicoding_events.domain.EventPreview

data class PastEventsState(
  val events: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
  val isSearching: Boolean = false,
  val searchQuery: String = "",
) {
  companion object {
    val Initial = PastEventsState(emptyList())

    val Fetching = PastEventsState(emptyList())

    fun loaded(events: List<EventPreview>) = PastEventsState(
      events,
      isFetching = false,
      isError = false
    )

    fun search(query: String) = PastEventsState(
      emptyList(),
      searchQuery = query,
      isFetching = false,
      isSearching = true
    )

    val Error = PastEventsState(
      emptyList(),
      isFetching = false,
      isError = true
    )
  }
}