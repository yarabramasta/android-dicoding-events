package dev.ybrmst.dicodingevents.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class PreferenceService(context: Context) {
  private val _store = context.dataStore

  companion object {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
      name = "settings"
    )

    val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
  }

  suspend fun setDarkMode(isDarkMode: Boolean) {
    _store.edit { settings ->
      settings[IS_DARK_MODE_KEY] = isDarkMode
    }
  }

  suspend fun getDarkMode(): Boolean {
    val settings = _store.data.first()
    return settings[IS_DARK_MODE_KEY] ?: false
  }
}