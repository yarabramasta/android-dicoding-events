package dev.ybrmst.dicoding_events.ui.viewmodel.past

sealed class PastEventsEvent {
  data object Fetching : PastEventsEvent()

  data object Refresh : PastEventsEvent()

  data class OnSearchQueryChange(val query: String) : PastEventsEvent()
}