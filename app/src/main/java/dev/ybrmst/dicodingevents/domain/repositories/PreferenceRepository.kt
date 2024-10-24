package dev.ybrmst.dicodingevents.domain.repositories

interface PreferenceRepository {
  suspend fun getDarkMode(): Boolean
  suspend fun setDarkMode(isDarkMode: Boolean)
}