package dev.ybrmst.dicoding_events.feat.event.domain

import dev.ybrmst.dicoding_events.utils.Resource
import kotlinx.coroutines.flow.Flow

interface EventRepository {
  suspend fun getHighlightedEvents(): Flow<Resource<List<Event>>>

  suspend fun searchEvents(
    status: Int,
    query: String,
  ): Flow<Resource<List<Event>>>

  suspend fun getUpcomingEvents(): Flow<Resource<List<Event>>>

  suspend fun getPastEvents(): Flow<Resource<List<Event>>>

  suspend fun getEventDetail(eventId: String): Flow<Resource<Event>>
}