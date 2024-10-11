package dev.ybrmst.dicoding_events.ui.viewmodel.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.launch

class EventDetailViewModel(private val repo: EventsRepository) : ViewModel() {
  private var _state by mutableStateOf(EventDetailState.Initial)

  val state: EventDetailState
    get() = _state

  fun fetchEventDetail(id: Int) {
    viewModelScope.launch {
      repo
        .getEventDetail(id)
        .collect { event ->
          _state = when (event) {
            is Resource.Loading -> EventDetailState.Initial
            is Resource.Error -> EventDetailState.Error
            is Resource.Success -> event.data?.let { EventDetailState.loaded(it) }
              ?: EventDetailState.Error
          }
        }
    }
  }
}