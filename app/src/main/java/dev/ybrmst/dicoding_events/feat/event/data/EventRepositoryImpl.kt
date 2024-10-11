package dev.ybrmst.dicoding_events.feat.event.data

import dev.ybrmst.dicoding_events.feat.event.domain.Event
import dev.ybrmst.dicoding_events.feat.event.domain.EventRepository
import dev.ybrmst.dicoding_events.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class EventRepositoryImpl(private val api: EventApi) : EventRepository {
  override suspend fun getHighlightedEvents(): Flow<Resource<List<Event>>> {
    return flow {
      emit(Resource.Loading())

      try {
        val res = api.getEvents(active = 1, limit = 5)
        val events = res.body()?.data ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }

      emit(Resource.Loading(isLoading = false))
    }
  }

  override suspend fun searchEvents(
    status: Int,
    query: String,
  ): Flow<Resource<List<Event>>> {
    return flow {
      emit(Resource.Loading())

      try {
        val res = api.getEvents(query = query, active = status)
        val events = res.body()?.data ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }

      emit(Resource.Loading(isLoading = false))
    }
  }

  override suspend fun getUpcomingEvents(): Flow<Resource<List<Event>>> {
    return flow {
      emit(Resource.Loading())

      try {
        val res = api.getEvents(active = 1)
        val events = res.body()?.data ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }

      emit(Resource.Loading(isLoading = false))
    }
  }

  override suspend fun getPastEvents(): Flow<Resource<List<Event>>> {
    return flow {
      emit(Resource.Loading())

      try {
        val res = api.getEvents(active = 0)
        val events = res.body()?.data ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }

      emit(Resource.Loading(isLoading = false))
    }
  }

  override suspend fun getEventDetail(
    eventId: String,
  ): Flow<Resource<Event>> {
    return flow {
      emit(Resource.Loading())

      try {
        val res = api.getEventDetail(eventId)
        val event = res.body()?.data?.firstOrNull()
        if (event != null) {
          emit(Resource.Success(event))
        } else {
          emit(Resource.Error("No event found."))
        }
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }

      emit(Resource.Loading(isLoading = false))
    }
  }
}