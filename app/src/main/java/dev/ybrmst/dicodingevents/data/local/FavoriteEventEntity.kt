package dev.ybrmst.dicodingevents.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "event_preview",
  indices = [Index(value = ["event_id"], unique = true)]
)
data class FavoriteEventEntity(
  @PrimaryKey(autoGenerate = true) val id: Int,

  @ColumnInfo(name = "event_id") val eventId: Int,

  @ColumnInfo(index = true) val name: String,

  val summary: String,

  @ColumnInfo(name = "city_name") val cityName: String,

  @ColumnInfo(name = "image_logo") val imageLogo: String,

  @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
)