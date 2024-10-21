package dev.ybrmst.dicoding_events.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicoding_events.domain.models.Result
import dev.ybrmst.dicoding_events.domain.repositories.EventsRepository
import dev.ybrmst.dicoding_events.ui.lib.BaseViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: EventsRepository,
) :
  BaseViewModel<HomeReducer.HomeState, HomeReducer.HomeEvent, HomeReducer.HomeEffect>(
    initialState = HomeReducer.HomeState.initial(),
    reducer = HomeReducer(),
  ) {

  init {
    onFetch()
  }

  fun onRefresh() = viewModelScope.launch { fetchEvents(refresh = true) }

  fun onFetch() = viewModelScope.launch { fetchEvents() }

  fun onEventClick(eventId: Int) {
    sendEffect(HomeReducer.HomeEffect.NavigateToEventDetail(eventId))
  }

  private suspend fun fetchEvents(refresh: Boolean = false) = coroutineScope {
    val events = repo.fetchEvents()

    events.collect { result ->
      when (result) {
        is Result.Success -> {
          val (upcomingEvents, finishedEvents) = result.data
          sendEvent(
            HomeReducer.HomeEvent.OnLoaded(
              upcomingEvents,
              finishedEvents
            )
          )
        }

        is Result.Error -> {
          sendEventForEffect(HomeReducer.HomeEvent.OnError(result.error))
        }

        Result.Loading -> {
          sendEvent(
            if (refresh) HomeReducer.HomeEvent.OnRefresh
            else HomeReducer.HomeEvent.OnFetch
          )
        }
      }
    }
  }
}