package dev.ybrmst.dicodingevents.data.repositories

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.suspendMapSuccess
import dev.ybrmst.dicodingevents.data.local.FavoriteEventDao
import dev.ybrmst.dicodingevents.data.local.toPreview
import dev.ybrmst.dicodingevents.data.network.EventsNetworkDataSource
import dev.ybrmst.dicodingevents.di.IoDispatcher
import dev.ybrmst.dicodingevents.domain.models.AppError
import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.models.toEntity
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepositoryImpl @Inject constructor(
  private val api: EventsNetworkDataSource,
  private val dao: FavoriteEventDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : EventsRepository {
  override fun getUpcomingFinishedEvents(): Flow<ApiResponse<Pair<List<EventPreview>, List<EventPreview>>>> {
    return api.getUpcomingFinishedEvents().map {
      it.suspendMapSuccess {
        val favEvents = dao
          .getAllFavoriteEvents()
          .map { event -> event.eventId }

        Pair(
          first.map { event -> event.copy(isFavorite = favEvents.contains(event.id)) },
          second.map { event -> event.copy(isFavorite = favEvents.contains(event.id)) }
        )
      }
    }.flowOn(ioDispatcher)
  }

  override fun queryEvents(
    active: Int,
    query: String?,
  ): Flow<ApiResponse<List<EventPreview>>> {
    return api.queryEvents(query = query, active = active).map {
      it.suspendMapSuccess {
        val favEvents = dao
          .getAllFavoriteEvents()
          .map { event -> event.eventId }

        this.map { event ->
          event.copy(isFavorite = favEvents.contains(event.id))
        }
      }
    }.flowOn(ioDispatcher)
  }

  override fun getFavEvents(): Flow<Pair<List<EventPreview>, AppError?>> {
    return flow {
      try {
        val favEvents = dao.getAllFavoriteEvents().map { it.toPreview() }
        emit(Pair(favEvents, null))
      } catch (e: Exception) {
        e.printStackTrace()
        emit(Pair(emptyList(), AppError.InternalServerError()))
      }
    }.flowOn(ioDispatcher)
  }

  override suspend fun addFavEvent(event: EventPreview): Pair<EventPreview, AppError?> {
    return withContext(ioDispatcher) {
      try {
        dao.insertFavoriteEvent(event.toEntity())
        Pair(event.copy(isFavorite = true), null)
      } catch (e: Exception) {
        e.printStackTrace()
        Pair(event, AppError.InternalServerError())
      }
    }
  }

  override suspend fun removeFavEvent(event: EventPreview): Pair<EventPreview, AppError?> {
    return withContext(ioDispatcher) {
      try {
        dao.deleteFavoriteEvent(event.id)
        Pair(event.copy(isFavorite = false), null)
      } catch (e: Exception) {
        e.printStackTrace()
        Pair(event, AppError.InternalServerError())
      }
    }
  }

  override suspend fun getEventDetail(id: Int): ApiResponse<EventDetail> {
    val detail = api.getEventDetail(id)

    if (detail is ApiResponse.Failure) return detail

    val favorite = dao.getFavoriteEventById(id)
    if (favorite != null) detail.mapSuccess { this.copy(isFavorite = true) }

    return detail
  }
}