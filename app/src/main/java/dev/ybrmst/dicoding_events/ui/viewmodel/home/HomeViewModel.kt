package dev.ybrmst.dicoding_events.ui.viewmodel.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: EventsRepository) : ViewModel() {

  private var _state by mutableStateOf(HomeState.Initial)

  val state: HomeState
    get() = _state

  fun add(event: HomeEvent) {
    when (event) {
      HomeEvent.OnRefresh -> onRefresh()
      HomeEvent.OnFetchEvents -> onFetchEvents()
    }
  }

  private fun onRefresh() {
    _state = HomeState.Fetching
    onFetchEvents()
  }

  private fun onFetchEvents() {

    viewModelScope.launch {
      _state = HomeState.Fetching

      val result = coroutineScope {

        val highlighted = async {
          repo.getEvents(active = 1, limit = 5)
        }.await()
        val upcoming = async { repo.getEvents(active = 1) }.await()

        Pair(highlighted, upcoming)
      }

      result.let { (res1, res2) ->

        val didErrorResults = res1 is Resource.Error || res2 is Resource.Error

        _state = if (didErrorResults) HomeState.Error
        else {
          HomeState.loaded(
            highlighted = res1.data ?: emptyList(),
            upcoming = res2.data ?: emptyList()
          )
        }
      }
    }
  }
}