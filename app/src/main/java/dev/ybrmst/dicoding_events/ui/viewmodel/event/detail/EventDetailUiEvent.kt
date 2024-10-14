package dev.ybrmst.dicoding_events.ui.viewmodel.event.detail

sealed class EventDetailUiEvent(open val id: Int) {

  data class OnFetch(override val id: Int) : EventDetailUiEvent(id)

  data class OnRefresh(override val id: Int) : EventDetailUiEvent(id)
}