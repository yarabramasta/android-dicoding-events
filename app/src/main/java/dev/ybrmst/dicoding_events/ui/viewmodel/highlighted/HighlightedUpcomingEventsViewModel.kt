package dev.ybrmst.dicoding_events.ui.viewmodel.highlighted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.launch

class HighlightedUpcomingEventsViewModel(
  private val repo: EventsRepository,
) : ViewModel() {
  private var _state by mutableStateOf<HighlightedUpcomingEventsState>(
    HighlightedUpcomingEventsState.Initial
  )

  val state: HighlightedUpcomingEventsState
    get() = _state

  init {
    add(HighlightedUpcomingEventsEvent.Fetching)
  }


  fun add(event: HighlightedUpcomingEventsEvent) {
    when (event) {
      is HighlightedUpcomingEventsEvent.Fetching -> fetchEvents()
      is HighlightedUpcomingEventsEvent.Refresh -> fetchEvents()
    }
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      repo
        .getHighlightedUpcomingEvents()
        .collect {
          _state = when (it) {
            is Resource.Loading -> HighlightedUpcomingEventsState.Fetching
            is Resource.Error -> HighlightedUpcomingEventsState.Error
            is Resource.Success -> HighlightedUpcomingEventsState.loaded(
              it.data ?: emptyList()
            )
          }
        }
    }
  }
}