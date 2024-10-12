package dev.ybrmst.dicoding_events.ui.viewmodel.home

import dev.ybrmst.dicoding_events.domain.EventPreview

data class HomeState(
  val highlighted: List<EventPreview>,
  val upcoming: List<EventPreview>,
  val isFetching: Boolean = true,
  val isError: Boolean = false,
) {

  companion object {
    private val Empty = HomeState(
      highlighted = emptyList(),
      upcoming = emptyList(),
      isFetching = true,
      isError = false
    )

    val Initial = Empty

    val Fetching = Empty.copy(isFetching = true)

    val Error = Empty.copy(isError = true)

    fun loaded(
      highlighted: List<EventPreview>,
      upcoming: List<EventPreview>,
    ) =
      HomeState(
        highlighted = highlighted,
        upcoming = upcoming,
        isFetching = false,
        isError = false,
      )
  }
}