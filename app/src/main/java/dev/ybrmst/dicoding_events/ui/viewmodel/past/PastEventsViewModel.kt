package dev.ybrmst.dicoding_events.ui.viewmodel.past

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PastEventsViewModel(
  private val repo: EventsRepository,
) : ViewModel() {
  private var _state by mutableStateOf<PastEventsState>(
    PastEventsState.Initial
  )

  val state: PastEventsState
    get() = _state

  private var searchJob: Job? = null

  init {
    add(PastEventsEvent.Fetching)
  }

  fun add(event: PastEventsEvent) {
    when (event) {
      is PastEventsEvent.Fetching -> fetchEvents()
      is PastEventsEvent.Refresh -> fetchEvents()
      is PastEventsEvent.OnSearchQueryChange -> {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(300L)
          searchEvents(event.query)
        }
      }
    }
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      repo
        .getPastEvents()
        .collect {
          _state = when (it) {
            is Resource.Loading -> PastEventsState.Fetching
            is Resource.Error -> PastEventsState.Error
            is Resource.Success -> PastEventsState.loaded(
              it.data ?: emptyList()
            )
          }
        }
    }
  }

  private fun searchEvents(query: String) {
    viewModelScope.launch {
      repo
        .searchEvents(status = 1, query = query)
        .collect {
          _state = when (it) {
            is Resource.Loading -> PastEventsState.search(query)
            is Resource.Error -> PastEventsState.Error
            is Resource.Success -> PastEventsState.loaded(
              it.data ?: emptyList()
            )
          }
        }
    }
  }
}