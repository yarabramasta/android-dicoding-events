package dev.ybrmst.dicoding_events.ui.viewmodel.event.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinishedEventsViewModel(
  private val repo: EventsRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(FinishedEventsUiState.Initial)

  val state: StateFlow<FinishedEventsUiState> get() = _state

  private var searchJob: Job? = null

  init {
    fetchEvents()
  }

  fun add(event: FinishedEventsUiEvent) {
    when (event) {
      is FinishedEventsUiEvent.OnFetch -> {
        _state.value = FinishedEventsUiState.Fetching
        fetchEvents()
      }

      is FinishedEventsUiEvent.OnRefresh -> {
        _state.value = FinishedEventsUiState.Refreshing
        fetchEvents()
      }

      is FinishedEventsUiEvent.OnQueryChanged -> {
        _state.value = FinishedEventsUiState.Initial.copy(query = event.query)

        searchJob?.cancel()

        _state.value = FinishedEventsUiState.Fetching.copy(query = event.query)

        searchJob = viewModelScope.launch {
          delay(500L)
          if (event.query.isEmpty()) {
            fetchEvents()
          } else {
            searchEvents(event.query)
          }
        }
      }

      is FinishedEventsUiEvent.OnQueryCleared -> {
        _state.value = FinishedEventsUiState.Refreshing
        fetchEvents()
      }
    }
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      val res = withContext(Dispatchers.IO) { repo.getEvents(active = 0) }

      _state.value = when (res) {
        is Resource.Success -> FinishedEventsUiState.loaded(
          res.data ?: emptyList()
        )

        is Resource.Error -> FinishedEventsUiState.Error
      }
    }
  }

  private fun searchEvents(q: String) {
    viewModelScope.launch {
      val res = withContext(Dispatchers.IO) {
        repo.getEvents(active = 0, query = q)
      }

      _state.value = when (res) {
        is Resource.Success -> FinishedEventsUiState.loaded(
          res.data ?: emptyList()
        ).copy(query = q)

        is Resource.Error -> FinishedEventsUiState.Error
      }
    }
  }
}