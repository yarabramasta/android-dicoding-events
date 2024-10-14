package dev.ybrmst.dicoding_events.ui.viewmodel.event.finished

sealed class FinishedEventsUiEvent {
  data object OnFetch : FinishedEventsUiEvent()

  data object OnRefresh : FinishedEventsUiEvent()

  data class OnQueryChanged(val query: String) : FinishedEventsUiEvent()

  data object OnQueryCleared : FinishedEventsUiEvent()
}