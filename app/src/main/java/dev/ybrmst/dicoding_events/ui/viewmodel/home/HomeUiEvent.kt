package dev.ybrmst.dicoding_events.ui.viewmodel.home

sealed class HomeUiEvent {
  data object OnRefresh : HomeUiEvent()

  data object OnFetch : HomeUiEvent()
}