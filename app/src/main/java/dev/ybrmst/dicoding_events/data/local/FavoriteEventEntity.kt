package dev.ybrmst.dicoding_events.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ybrmst.dicoding_events.domain.models.FavoriteEvent

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,

  @ColumnInfo(name = "event_id")
  val eventId: Int,

  @ColumnInfo(name = "name")
  val name: String,

  @ColumnInfo(name = "summary")
  val summary: String,

  @ColumnInfo(name = "city_name")
  val cityName: String,

  @ColumnInfo(name = "image_logo")
  val imageLogo: String,
)

fun FavoriteEventEntity.toDomain() = FavoriteEvent(
  id = eventId,
  name = name,
  summary = summary,
  cityName = cityName,
  imageLogo = imageLogo,
)