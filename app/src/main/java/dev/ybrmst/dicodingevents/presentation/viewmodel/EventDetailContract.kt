package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.lib.ViewModelContract

class EventDetailContract : ViewModelContract {
  @Immutable
  data class State(
    val isFetching: Boolean,
    val isRefreshing: Boolean,
    val error: String?,
    val event: EventDetail?,
  ) : ViewModelContract.State

  sealed class Event : ViewModelContract.Event {
    data class OnFetch(val eventId: Int) : Event()
    data class OnRefresh(val eventId: Int) : Event()
  }
}