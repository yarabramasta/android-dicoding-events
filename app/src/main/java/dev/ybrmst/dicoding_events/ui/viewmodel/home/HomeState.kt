package dev.ybrmst.dicoding_events.ui.viewmodel.home

import dev.ybrmst.dicoding_events.domain.EventPreview

data class HomeState(
  val upcomingEvents: List<EventPreview>,
  val pastEvents: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
) {

  companion object {
    private val Empty = HomeState(
      upcomingEvents = emptyList(),
      pastEvents = emptyList(),
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
        upcomingEvents = highlights,
        pastEvents = events,
        isFetching = false,
        isError = false,
      )
  }
}