package dev.ybrmst.dicodingevents.domain.repositories

import com.skydoves.sandwich.ApiResponse
import dev.ybrmst.dicodingevents.domain.models.AppError
import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
  fun getUpcomingFinishedEvents(): Flow<ApiResponse<Pair<List<EventPreview>, List<EventPreview>>>>

  fun queryEvents(
    active: Int,
    query: String? = null,
  ): Flow<ApiResponse<List<EventPreview>>>

  fun getFavEvents(): Flow<Pair<List<EventPreview>, AppError?>>

  suspend fun addFavEvent(event: EventPreview): Pair<EventPreview, AppError?>

  suspend fun removeFavEvent(event: EventPreview): Pair<EventPreview, AppError?>

  suspend fun getEventDetail(id: Int): ApiResponse<EventDetail>
}