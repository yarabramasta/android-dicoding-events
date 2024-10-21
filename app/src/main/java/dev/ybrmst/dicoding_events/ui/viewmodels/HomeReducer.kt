package dev.ybrmst.dicoding_events.ui.viewmodels

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicoding_events.domain.errors.FetchEventError
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.ui.lib.Reducer

class HomeReducer :
  Reducer<HomeReducer.HomeState, HomeReducer.HomeEvent, HomeReducer.HomeEffect> {

  @Immutable
  data class HomeState(
    val isFetching: Boolean,
    val isRefreshing: Boolean,
    val error: FetchEventError?,
    val upcomingEvents: List<EventPreview>,
    val finishedEvents: List<EventPreview>,
  ) : Reducer.ViewState {

    companion object {
      private val events = listOf(
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
      ).distinctBy { it.id }

      fun initial() = HomeState(
        isFetching = true,
        isRefreshing = false,
        error = null,
        upcomingEvents = events.take(2),
        finishedEvents = events.takeLast(3),
      )
    }
  }

  @Immutable
  sealed class HomeEvent : Reducer.ViewEvent {
    data object OnRefresh : HomeEvent()

    data object OnFetch : HomeEvent()

    data class OnError(val error: FetchEventError) : HomeEvent()

    data class OnLoaded(
      val upcomingEvents: List<EventPreview>,
      val finishedEvents: List<EventPreview>,
    ) : HomeEvent()


    data class OnEventClick(val eventId: Int) : HomeEvent()
  }

  @Immutable
  sealed class HomeEffect : Reducer.ViewEffect {
    data class NavigateToEventDetail(val eventId: Int) : HomeEffect()
  }

  override fun dispatch(
    state: HomeState,
    event: HomeEvent,
  ): Pair<HomeState, HomeEffect?> {
    return when (event) {
      is HomeEvent.OnFetch -> {
        HomeState.initial() to null
      }

      is HomeEvent.OnRefresh -> {
        HomeState.initial().copy(isRefreshing = true) to null
      }

      is HomeEvent.OnError -> {
        state.copy(
          isFetching = false,
          isRefreshing = false,
          error = event.error,
        ) to null
      }

      is HomeEvent.OnLoaded -> {
        state.copy(
          isFetching = false,
          isRefreshing = false,
          upcomingEvents = event.upcomingEvents,
          finishedEvents = event.finishedEvents,
        ) to null
      }

      is HomeEvent.OnEventClick -> {
        state to HomeEffect.NavigateToEventDetail(event.eventId)
      }
    }
  }
}