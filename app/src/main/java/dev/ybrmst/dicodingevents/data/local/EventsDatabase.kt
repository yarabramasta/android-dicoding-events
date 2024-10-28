package dev.ybrmst.dicodingevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [FavoriteEventEntity::class],
  version = 1,
  exportSchema = false
)
abstract class EventsDatabase : RoomDatabase() {
  abstract fun favEventDao(): FavoriteEventDao
}