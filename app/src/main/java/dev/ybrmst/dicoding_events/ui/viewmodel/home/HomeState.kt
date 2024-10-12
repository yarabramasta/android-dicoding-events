package dev.ybrmst.dicoding_events.ui.viewmodel.home

import dev.ybrmst.dicoding_events.domain.EventPreview

data class HomeState(
  val highlights: List<EventPreview>,
  val events: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
) {

  companion object {
    private val Empty = HomeState(
      highlights = emptyList(),
      events = emptyList(),
      isFetching = true,
      isError = false
    )

    val Initial = Empty

    val Fetching = Empty.copy(isFetching = true)

    val Error = Empty.copy(isError = true)

    fun loaded(
      highlights: List<EventPreview>,
      events: List<EventPreview>,
    ) =
      HomeState(
        highlights = highlights,
        events = events,
        isFetching = false,
        isError = false,
      )
  }
}