package dev.ybrmst.dicoding_events.ui.viewmodel.highlighted

sealed class HighlightedUpcomingEventsEvent {
  data object Fetching : HighlightedUpcomingEventsEvent()

  data object Refresh : HighlightedUpcomingEventsEvent()
}