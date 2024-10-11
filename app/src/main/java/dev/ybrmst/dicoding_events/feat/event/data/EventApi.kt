package dev.ybrmst.dicoding_events.feat.event.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {
  @GET("events")
  suspend fun getEvents(
    @Query("active") active: Int = -1,
    @Query("limit") limit: Int? = null,
    @Query("q") query: String? = null,
  ): Response<EventResponse>

  @GET("events/{id}")
  suspend fun getEventDetail(@Path("id") id: String): Response<EventResponse>

  companion object {
    const val BASE_URL = "https://event-api.dicoding.dev"
  }
}