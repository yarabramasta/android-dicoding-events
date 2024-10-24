package dev.ybrmst.dicodingevents.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.ybrmst.dicodingevents.domain.repositories.PreferenceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {

  companion object {
    val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
  }

  override suspend fun setDarkMode(isDarkMode: Boolean) {
    dataStore.edit { settings ->
      settings[IS_DARK_MODE_KEY] = isDarkMode
    }
  }

  override suspend fun getDarkMode(): Boolean {
    val settings = dataStore.data.first()
    return settings[IS_DARK_MODE_KEY] ?: false
  }
}