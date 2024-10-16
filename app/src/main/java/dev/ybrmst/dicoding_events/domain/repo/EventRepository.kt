package dev.ybrmst.dicoding_events.domain.repo

import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.model.EventDetail
import dev.ybrmst.dicoding_events.domain.model.EventPreview

interface EventRepository {
  suspend fun getEvents(
    active: Int? = -1,
    limit: Int = 20,
    query: String? = null,
  ): Resource<List<EventPreview>>

  suspend fun getEventDetail(eventId: Int): Resource<EventDetail>
}