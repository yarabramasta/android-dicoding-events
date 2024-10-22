package dev.ybrmst.dicoding_events.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
  @Query("SELECT * FROM favorite_events")
  fun getAllFavoriteEvents(): Flow<List<FavoriteEventEntity>>

  @Insert
  suspend fun addEvent(event: FavoriteEventEntity)

  @Delete
  suspend fun removeEvent(event: FavoriteEventEntity)
}