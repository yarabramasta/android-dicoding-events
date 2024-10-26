package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.MainApp
import dev.ybrmst.dicodingevents.data.local.PreferencesDataSource
import dev.ybrmst.dicodingevents.data.local.ThemeMode
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val dataSource: PreferencesDataSource,
  private val application: MainApp,
) : BaseViewModel<
        SettingsContract.State,
        SettingsContract.Event,
        SettingsContract.Effect>
  (initialState = SettingsContract.State.initial()) {


  override fun add(event: SettingsContract.Event) {
    when (event) {
      SettingsContract.Event.OnFetch -> fetchSettings()
      is SettingsContract.Event.OnSetThemeMode -> setThemeMode(event.darkMode)
      SettingsContract.Event.OnToggleDailyNotifOptIn -> toggleDailyNotif()
    }
  }

  private fun fetchSettings() = runBlocking {
    val themeMode = dataSource.themeFlow.first()
    val isOptInDailyNotif = dataSource.dailyNotifOptInFlow.first()

    setState {
      copy(
        themeMode = themeMode,
        isOptInDailyNotif = isOptInDailyNotif
      )
    }

    application.settings.value = application
      .settings
      .value
      .copy(themeMode = themeMode, isOptInDailyNotif = isOptInDailyNotif)
  }

  private fun setThemeMode(darkMode: Boolean) {
    viewModelScope.launch {
      val updatedMode = if (darkMode) ThemeMode.DARK else ThemeMode.LIGHT

      dataSource.setThemeMode(updatedMode)
      application.settings.value = application
        .settings
        .value
        .copy(themeMode = updatedMode)

      setState { copy(themeMode = updatedMode) }
    }
  }

  private fun toggleDailyNotif() {
    viewModelScope.launch {
      val isOptIn = dataSource.dailyNotifOptInFlow.first()

      dataSource.setDailyNotifOptIn(!isOptIn)
      application.settings.value = application
        .settings
        .value
        .copy(isOptInDailyNotif = !isOptIn)

      setState { copy(isOptInDailyNotif = !isOptIn) }
    }
  }
}