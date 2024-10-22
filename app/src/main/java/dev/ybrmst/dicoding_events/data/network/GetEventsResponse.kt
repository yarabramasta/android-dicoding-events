package dev.ybrmst.dicoding_events.data.network

import dev.ybrmst.dicoding_events.domain.models.EventPreview

data class GetEventsResponse(
  val error: Boolean,
  val message: String?,
  val listEvents: List<EventPreview>,
)