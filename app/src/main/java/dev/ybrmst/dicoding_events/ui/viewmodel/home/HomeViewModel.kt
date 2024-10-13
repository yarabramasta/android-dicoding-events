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

  private val _state = MutableStateFlow(HomeState.Initial)

  val state: StateFlow<HomeState> get() = _state

  init {
    onFetchEvents()
  }

  fun add(event: HomeEvent) {
    when (event) {
      HomeEvent.OnFetch -> {
        _state.value = HomeState.Fetching
        onFetchEvents()
      }
      HomeEvent.OnRefresh -> {
        _state.value = HomeState.Refreshing
        onFetchEvents()
      }
    }
  }

  private fun onFetchEvents() {

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
          HomeState.Error
        } else {
          HomeState.loaded(
            highlights = res1.data ?: emptyList(),
            events = res2.data ?: emptyList()
          )
        }
      }
    }
  }
}