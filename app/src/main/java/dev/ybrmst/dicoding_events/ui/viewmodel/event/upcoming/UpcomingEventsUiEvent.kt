package dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming

sealed class UpcomingEventsUiEvent {
  data object OnFetch : UpcomingEventsUiEvent()

  data object OnRefresh : UpcomingEventsUiEvent()

  data class OnQueryChanged(val query: String) : UpcomingEventsUiEvent()

  data object OnQueryCleared : UpcomingEventsUiEvent()
}