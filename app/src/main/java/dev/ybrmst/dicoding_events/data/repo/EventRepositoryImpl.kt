package dev.ybrmst.dicoding_events.data.repo

import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.data.network.EventsApi
import dev.ybrmst.dicoding_events.domain.error.FatalError
import dev.ybrmst.dicoding_events.domain.error.FetchEventDetailError
import dev.ybrmst.dicoding_events.domain.error.FetchEventDetailNetworkError
import dev.ybrmst.dicoding_events.domain.error.FetchEventDetailNotFoundError
import dev.ybrmst.dicoding_events.domain.error.FetchEventsError
import dev.ybrmst.dicoding_events.domain.error.FetchEventsNetworkError
import dev.ybrmst.dicoding_events.domain.model.EventDetail
import dev.ybrmst.dicoding_events.domain.model.EventPreview
import dev.ybrmst.dicoding_events.domain.repo.EventRepository
import java.io.IOException

class EventRepositoryImpl(
  private val api: EventsApi,
) : EventRepository {
  override suspend fun getEvents(
    active: Int?,
    limit: Int,
    query: String?,
  ): Resource<List<EventPreview>> {
    return try {
      val res = api.getEvents(
        active = active ?: -1,
        query = query,
        limit = limit,
      )

      if (!res.isSuccessful) {
        Resource.Error(FetchEventsError())
      } else {
        val data = res.body()
        data?.let { Resource.Success(it.listEvents) } ?: Resource.Error(
          FetchEventsError()
        )
      }
    } catch (e: IOException) {
      Resource.Error(FetchEventsNetworkError())
    } catch (e: Exception) {
      Resource.Error(FatalError())
    }
  }

  override suspend fun getEventDetail(eventId: Int): Resource<EventDetail> {
    return try {
      val res = api.getEventDetail(eventId)

      if (!res.isSuccessful) {
        Resource.Error(FetchEventDetailError())
      } else {
        val data = res.body()?.event
        data?.let { Resource.Success(it) } ?: Resource.Error(
          FetchEventDetailNotFoundError()
        )
      }
    } catch (e: IOException) {
      Resource.Error(FetchEventDetailNetworkError())
    } catch (e: Exception) {
      Resource.Error(FatalError())
    }
  }
}