package dev.ybrmst.dicoding_events.feat.event.data

import dev.ybrmst.dicoding_events.feat.event.domain.Event

data class EventResponse(
  val error: Boolean,
  val message: String?,
  val data: List<Event>,
)