package dev.ybrmst.dicoding_events.data.network

import dev.ybrmst.dicoding_events.domain.EventPreview

data class EventsResponse(
  val error: Boolean,
  val message: String?,
  val listEvents: List<EventPreview>,
)