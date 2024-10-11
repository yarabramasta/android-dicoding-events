package dev.ybrmst.dicoding_events.ui.viewmodel.upcoming

sealed class UpcomingEventsEvent {
  data object Fetching : UpcomingEventsEvent()

  data object Refresh : UpcomingEventsEvent()

  data class OnSearchQueryChange(val query: String) : UpcomingEventsEvent()
}