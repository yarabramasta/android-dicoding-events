package dev.ybrmst.dicoding_events.ui.viewmodel.upcoming

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

class UpcomingEventsViewModel(
  private val repo: EventsRepository,
) : ViewModel() {
  private var _state by mutableStateOf<UpcomingEventsState>(
    UpcomingEventsState.Initial
  )

  val state: UpcomingEventsState
    get() = _state

  private var searchJob: Job? = null

  init {
    add(UpcomingEventsEvent.Fetching)
  }

  fun add(event: UpcomingEventsEvent) {
    when (event) {
      is UpcomingEventsEvent.Fetching -> fetchEvents()
      is UpcomingEventsEvent.Refresh -> fetchEvents()
      is UpcomingEventsEvent.OnSearchQueryChange -> {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(300)
          searchEvents(event.query)
        }
      }
    }
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      repo
        .getUpcomingEvents()
        .collect {
          _state = when (it) {
            is Resource.Loading -> UpcomingEventsState.Fetching
            is Resource.Error -> UpcomingEventsState.Error
            is Resource.Success -> UpcomingEventsState.loaded(
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
            is Resource.Loading -> UpcomingEventsState.search(query)
            is Resource.Error -> UpcomingEventsState.Error
            is Resource.Success -> UpcomingEventsState.loaded(
              it.data ?: emptyList()
            )
          }
        }
    }
  }
}