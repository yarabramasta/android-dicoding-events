package dev.ybrmst.dicoding_events.ui.viewmodel.highlighted

import dev.ybrmst.dicoding_events.domain.EventPreview

data class HighlightedUpcomingEventsState(
  val events: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
) {
  companion object {
    val Initial = HighlightedUpcomingEventsState(emptyList())

    val Fetching = HighlightedUpcomingEventsState(emptyList())

    fun loaded(events: List<EventPreview>) = HighlightedUpcomingEventsState(
      events,
      isFetching = false,
      isError = false
    )

    val Error = HighlightedUpcomingEventsState(
      emptyList(),
      isFetching = false,
      isError = true
    )
  }
}