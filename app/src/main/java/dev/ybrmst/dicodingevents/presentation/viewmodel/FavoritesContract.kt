package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.lib.ViewModelContract

class FavoritesContract : ViewModelContract {

  @Immutable
  data class State(
    val favorites: List<EventPreview>,
    val isFetching: Boolean,
    val isRefreshing: Boolean,
    val error: String? = null,
  ) : ViewModelContract.State {
    companion object {
      private val events = listOf(
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
      ).distinctBy { it.id }

      fun initial() = State(
        favorites = events,
        isFetching = false,
        isRefreshing = false,
      )
    }
  }

  sealed class Event : ViewModelContract.Event {
    data object OnFetch : Event()
    data object OnRefresh : Event()
    data class OnEventClicked(val eventId: Int) : Event()
    data class OnEventRemoved(val event: EventPreview) : Event()
    data object OnScreenChanged : Event()
  }

  sealed class Effect : ViewModelContract.Effect {
    data class NavigateToDetail(val eventId: Int) : Effect()
    data class ShowToast(val message: String) : Effect()
  }
}