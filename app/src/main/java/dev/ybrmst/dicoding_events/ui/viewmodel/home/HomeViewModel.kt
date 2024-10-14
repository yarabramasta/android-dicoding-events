package dev.ybrmst.dicoding_events.ui.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: EventsRepository) : ViewModel() {

  private val _state = MutableStateFlow(HomeUiState.Initial)

  val state: StateFlow<HomeUiState> get() = _state

  init {
    fetchEvents()
  }

  fun add(event: HomeUiEvent) {
    when (event) {
      HomeUiEvent.OnFetch -> {
        _state.value = HomeUiState.Fetching
        fetchEvents()
      }
      HomeUiEvent.OnRefresh -> {
        _state.value = HomeUiState.Refreshing
        fetchEvents()
      }
    }
  }

  private fun fetchEvents() {

    viewModelScope.launch {

      val result = coroutineScope {
        val upcomingEvents = async {
          repo.getEvents(active = 1, limit = 5)
        }.await()
        val pastEvents = async {
          repo.getEvents(active = 0, limit = 5)
        }.await()

        Pair(upcomingEvents, pastEvents)
      }

      result.let { (res1, res2) ->
        val didErrorResults = res1 is Resource.Error || res2 is Resource.Error

        _state.value = if (didErrorResults) {
          HomeUiState.Error
        } else {
          HomeUiState.loaded(
            highlights = res1.data ?: emptyList(),
            events = res2.data ?: emptyList()
          )
        }
      }
    }
  }
}