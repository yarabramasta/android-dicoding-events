package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.lib.ViewModelContract

class FavoritesContract : ViewModelContract {

  @Immutable
  data class State(
    val events: List<EventPreview>,
    val error: String? = null,
  ) : ViewModelContract.State {
    companion object {
      fun initial() = State(events = emptyList())
    }
  }

  sealed class Event : ViewModelContract.Event {
    data object OnFetch : Event()
    data class OnEventAdded(val event: EventPreview) : Event()
    data class OnEventRemoved(val event: EventPreview) : Event()
  }

  sealed class Effect : ViewModelContract.Effect {
    data class ShowToast(val message: String) : Effect()
  }
}