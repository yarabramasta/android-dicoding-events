package dev.ybrmst.dicodingevents.data.network

import dev.ybrmst.dicodingevents.domain.models.EventDetail
import kotlinx.serialization.Serializable

@Serializable
data class GetEventsApiResponse(
  val error: Boolean,
  val message: String?,
  val listEvents: List<EventDetail>,
)