package dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpcomingEventsViewModel(
  private val repo: EventsRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(UpcomingEventsUiState.Initial)

  val state: StateFlow<UpcomingEventsUiState> get() = _state

  private var searchJob: Job? = null

  init {
    fetchEvents()
  }

  fun add(event: UpcomingEventsUiEvent) {
    when (event) {
      is UpcomingEventsUiEvent.OnFetch -> {
        _state.value = UpcomingEventsUiState.Fetching
        fetchEvents()
      }

      is UpcomingEventsUiEvent.OnRefresh -> {
        _state.value = UpcomingEventsUiState.Refreshing
        fetchEvents()
      }

      is UpcomingEventsUiEvent.OnQueryChanged -> {
        _state.value = UpcomingEventsUiState.Fetching.copy(query = event.query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(500L)
          if (event.query.isEmpty()) {
            fetchEvents()
          } else {
            searchEvents(event.query)
          }
        }
      }

      is UpcomingEventsUiEvent.OnQueryCleared -> {
        _state.value = UpcomingEventsUiState.Refreshing
        fetchEvents()
      }
    }
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      val res = repo.getEvents(active = 1)
      _state.value = when (res) {
        is Resource.Success -> UpcomingEventsUiState.loaded(
          res.data ?: emptyList()
        )

        is Resource.Error -> UpcomingEventsUiState.Error
      }
    }
  }

  private fun searchEvents(q: String) {
    viewModelScope.launch {
      val res = repo.getEvents(active = 1, query = q)
      _state.value = when (res) {
        is Resource.Success -> UpcomingEventsUiState.loaded(
          res.data ?: emptyList()
        ).copy(query = q)

        is Resource.Error -> UpcomingEventsUiState.Error
      }
    }
  }
}