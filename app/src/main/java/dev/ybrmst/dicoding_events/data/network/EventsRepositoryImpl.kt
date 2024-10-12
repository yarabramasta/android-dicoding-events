package dev.ybrmst.dicoding_events.data.network

import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventDetail
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.domain.EventsRepository
import dev.ybrmst.dicoding_events.domain.errors.FatalError
import dev.ybrmst.dicoding_events.domain.errors.FetchEventDetailError
import dev.ybrmst.dicoding_events.domain.errors.FetchEventDetailNetworkError
import dev.ybrmst.dicoding_events.domain.errors.FetchEventDetailNotFoundError
import dev.ybrmst.dicoding_events.domain.errors.FetchEventsError
import dev.ybrmst.dicoding_events.domain.errors.FetchEventsNetworkError
import java.io.IOException

class EventsRepositoryImpl(
  private val api: EventsApi,
) : EventsRepository {
  override suspend fun getEvents(
    active: Int?,
    limit: Int?,
    query: String?
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