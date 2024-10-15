package dev.ybrmst.dicoding_events.ui.viewmodel.event.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventDetailViewModel(
  private val repo: EventsRepository,
) : ViewModel() {
  private val _state = MutableStateFlow(EventDetailUiState.Initial)

  val state: StateFlow<EventDetailUiState> get() = _state

  fun add(event: EventDetailUiEvent) {
    when (event) {
      is EventDetailUiEvent.OnFetch -> {
        _state.value = EventDetailUiState.Fetching
        fetchEventDetail(event.id)
      }

      is EventDetailUiEvent.OnRefresh -> {
        _state.value = EventDetailUiState.Refreshing
        fetchEventDetail(event.id)
      }
    }
  }

  private fun fetchEventDetail(id: Int) {
    viewModelScope.launch {
      val res = withContext(Dispatchers.IO) { repo.getEventDetail(id) }

      _state.value = when (res) {
        is Resource.Success -> res.data?.let { EventDetailUiState.loaded(it) }
          ?: EventDetailUiState.Error

        is Resource.Error -> EventDetailUiState.Error
      }
    }
  }
}