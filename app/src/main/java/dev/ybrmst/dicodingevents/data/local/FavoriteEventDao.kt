package dev.ybrmst.dicodingevents.data.local

import androidx.room.Dao
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

  @Query("DELETE FROM fav_events WHERE event_id = :eventId")
  fun deleteFavoriteEvent(eventId: Int)
}