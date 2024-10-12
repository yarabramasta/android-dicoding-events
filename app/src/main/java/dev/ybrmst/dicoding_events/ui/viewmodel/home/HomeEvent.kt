package dev.ybrmst.dicoding_events.ui.viewmodel.home

sealed class HomeEvent {
  data object OnRefresh : HomeEvent()

  data object OnFetchEvents : HomeEvent()
}