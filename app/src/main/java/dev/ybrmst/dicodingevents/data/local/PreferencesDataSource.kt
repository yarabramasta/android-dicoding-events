package dev.ybrmst.dicodingevents.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
  SYSTEM, LIGHT, DARK
}

@Singleton
class PreferencesDataSource @Inject constructor(
  @ApplicationContext private val context: Context,
) {

  companion object {
    const val SETTINGS_DATASTORE_NAME = "_dicoding_events_app_settings"

    val THEME_MODE_PREFERENCES_KEY_NAME = intPreferencesKey("_app_theme_mode")
    val DAILY_NOTIF_PREFERENCES_KEY_NAME =
      booleanPreferencesKey("_app_daily_notif")
  }

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_DATASTORE_NAME
  )

  val themeFlow: Flow<ThemeMode>
    get() = context
      .dataStore
      .data
      .map { it[THEME_MODE_PREFERENCES_KEY_NAME] ?: 0 }
      .map { ThemeMode.entries[it] }

  val dailyNotifOptInFlow: Flow<Boolean>
    get() = context
      .dataStore
      .data
      .map { it[DAILY_NOTIF_PREFERENCES_KEY_NAME] ?: false }

  suspend fun setThemeMode(themeMode: ThemeMode) {
    context.dataStore.edit { settings ->
      settings[THEME_MODE_PREFERENCES_KEY_NAME] = themeMode.ordinal
    }
  }

  suspend fun setDailyNotifOptIn(isOptIn: Boolean) {
    context.dataStore.edit { settings ->
      settings[DAILY_NOTIF_PREFERENCES_KEY_NAME] = isOptIn
    }
  }
}