package dev.ybrmst.dicoding_events.ui.viewmodel.event.detail

sealed class DetailEvent(open val id: Int) {

  data class OnFetch(override val id: Int) : DetailEvent(id)

  data class OnRefresh(override val id: Int) : DetailEvent(id)
}