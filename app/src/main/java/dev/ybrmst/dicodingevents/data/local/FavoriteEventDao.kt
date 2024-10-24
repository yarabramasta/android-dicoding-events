package dev.ybrmst.dicodingevents.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {
  @Query("SELECT * FROM fav_events")
  fun getAllFavoriteEvents(): List<FavoriteEventEntity>

  @Query("SELECT * FROM fav_events WHERE event_id = :eventId")
  fun getFavoriteEventById(eventId: Int): FavoriteEventEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertFavoriteEvent(favoriteEventEntity: FavoriteEventEntity)

  @Delete
  fun deleteFavoriteEvent(favoriteEventEntity: FavoriteEventEntity)
}