package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.lib.ViewModelContract

class DiscoverContract : ViewModelContract {
  enum class EventCategory {
    UPCOMING,
    FINISHED
  }

  @Immutable
  data class State(
    val upcomingEvents: List<EventPreview>,
    val isFetchingUpcoming: Boolean,
    val isRefreshingUpcoming: Boolean,
    val isSearchingUpcoming: Boolean,
    val errorUpcoming: String?,
    val searchQueryUpcoming: String,
    val finishedEvents: List<EventPreview>,
    val isFetchingFinished: Boolean,
    val isRefreshingFinished: Boolean,
    val isSearchingFinished: Boolean,
    val errorFinished: String?,
    val searchQueryFinished: String,
  ) : ViewModelContract.State {
    companion object {
      private val events = listOf(
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
      ).distinctBy { it.id }

      fun initial() = State(
        upcomingEvents = events.take(5),
        isFetchingUpcoming = false,
        isRefreshingUpcoming = false,
        isSearchingUpcoming = false,
        errorUpcoming = null,
        searchQueryUpcoming = "",
        finishedEvents = events.takeLast(5),
        isFetchingFinished = false,
        isRefreshingFinished = false,
        isSearchingFinished = false,
        errorFinished = null,
        searchQueryFinished = "",
      )
    }
  }

  sealed class Event : ViewModelContract.Event {
    data class OnFetching(val category: EventCategory) : Event()

    data class OnRefreshing(val category: EventCategory) : Event()

    data class OnSearchQueryChanged(
      val query: String,
      val category: EventCategory,
    ) : Event()

    data class OnEventClicked(val eventId: Int) : Event()

    data class OnEventFavoriteChanged(
      val event: EventPreview,
      val category: EventCategory,
    ) : Event()

    data object OnScreenChanged : Event()
  }

  sealed class Effect : ViewModelContract.Effect {
    data class NavigateToEventDetail(val eventId: Int) : Effect()
    data class ShowToast(val message: String) : Effect()
  }
}