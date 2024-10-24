package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.domain.models.EventPreview

interface HomeContract {
  @Immutable
  data class State(
    val isFetching: Boolean,
    val isRefreshing: Boolean,
    val error: String? = null,
    val upcomingEvents: List<EventPreview>,
    val finishedEvents: List<EventPreview>,
  ) {
    companion object {
      private val events = listOf(
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
      ).distinctBy { it.id }

      fun initial() = State(
        isFetching = false,
        isRefreshing = false,
        upcomingEvents = events.take(2),
        finishedEvents = events.takeLast(3)
      )
    }
  }

  sealed class Event {
    data object OnFetch : Event()
    data object OnRefresh : Event()
    data class OnEventClicked(val eventId: Int) : Event()
    data class OnEventFavoriteChanged(val event: EventPreview) : Event()
  }

  sealed class Effect {
    data class NavigateToDetail(val eventId: Int) : Effect()
    data class ShowToast(val message: String) : Effect()
  }
}