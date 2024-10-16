package dev.ybrmst.dicoding_events.data.network

import dev.ybrmst.dicoding_events.domain.model.EventDetail

data class EventDetailResponse(
  val error: Boolean,
  val message: String?,
  val event: EventDetail,
)