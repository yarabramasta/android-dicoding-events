package dev.ybrmst.dicoding_events.feat.event.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.feat.event.domain.EventRepository
import dev.ybrmst.dicoding_events.utils.Resource
import kotlinx.coroutines.launch

class EventsViewModel(
  private val repo: EventRepository,
) : ViewModel() {
  private var state by mutableStateOf(EventsState())

  val highlightedEvents get() = state.highlightedEvents
  val upcomingEvents get() = state.upcomingEvents
  val pastEvents get() = state.pastEvents

  val error get() = state.error

  val isLoading get() = state.isLoading

  fun onEvent(event: EventsEvent) {
    when (event) {
      is EventsEvent.LoadHighlighted -> onLoadHighlighted()
      is EventsEvent.LoadUpcoming -> onLoadUpcoming()
      is EventsEvent.LoadPast -> onLoadPast()
    }
  }

  private fun onLoadHighlighted() {
    state = state.copy(isLoading = true)
    viewModelScope.launch {
      repo.getHighlightedEvents().collect { res ->
        state = when (res) {
          is Resource.Loading -> {
            state.copy(isLoading = res.isLoading)
          }

          is Resource.Success -> {
            state.copy(highlightedEvents = res.data ?: emptyList())
          }

          is Resource.Error -> {
            state.copy(
              error = res.message ?: "Uh oh, something went wrong..."
            )
          }
        }
      }
    }
  }

  private fun onLoadUpcoming() {
    state = state.copy(isLoading = true)
    viewModelScope.launch {
      repo.getUpcomingEvents().collect { res ->
        state = when (res) {
          is Resource.Loading -> {
            state.copy(isLoading = res.isLoading)
          }

          is Resource.Success -> {
            state.copy(upcomingEvents = res.data ?: emptyList())
          }

          is Resource.Error -> {
            state.copy(
              error = res.message ?: "Uh oh, something went wrong..."
            )
          }
        }
      }
    }
  }

  private fun onLoadPast() {
    state = state.copy(isLoading = true)
    viewModelScope.launch {
      repo.getPastEvents().collect { res ->
        state = when (res) {
          is Resource.Loading -> {
            state.copy(isLoading = res.isLoading)
          }

          is Resource.Success -> {
            state.copy(pastEvents = res.data ?: emptyList())
          }

          is Resource.Error -> {
            state.copy(error = res.message)
          }
        }
      }
    }
  }
}