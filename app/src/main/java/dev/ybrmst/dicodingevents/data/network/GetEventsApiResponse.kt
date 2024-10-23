package dev.ybrmst.dicodingevents.data.network

import dev.ybrmst.dicodingevents.domain.models.EventDetail

data class GetEventsApiResponse(
  val error: Boolean,
  val message: String,
  val listEvents: List<EventDetail>,
)