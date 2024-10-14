package dev.ybrmst.dicoding_events.ui.viewmodel.event.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
  private val repo: EventsRepository,
) : ViewModel() {
  private val _state = MutableStateFlow(DetailState.Initial)

  val state: StateFlow<DetailState> get() = _state

  fun add(event: DetailEvent) {
    when (event) {
      is DetailEvent.OnFetch -> {
        _state.value = DetailState.Fetching
        fetchEventDetail(event.id)
      }

      is DetailEvent.OnRefresh -> {
        _state.value = DetailState.Refreshing
        fetchEventDetail(event.id)
      }
    }
  }

  private fun fetchEventDetail(id: Int) {
    viewModelScope.launch {
      val res = repo.getEventDetail(id)
      _state.value = when (res) {
        is Resource.Success -> res.data?.let { DetailState.loaded(it) }
          ?: DetailState.Error

        is Resource.Error -> DetailState.Error
      }
    }
  }
}