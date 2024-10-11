package dev.ybrmst.dicoding_events.ui.viewmodel.detail

import dev.ybrmst.dicoding_events.domain.EventDetail

data class EventDetailState(
  val event: EventDetail? = null,
  val isLoading: Boolean = true,
  val isError: Boolean = false,
) {
  companion object {
    val Initial = EventDetailState()

    fun loaded(event: EventDetail): EventDetailState =
      EventDetailState(
        event = event,
        isLoading = false,
        isError = false
      )

    val Error = EventDetailState(isLoading = false, isError = true)
  }
}