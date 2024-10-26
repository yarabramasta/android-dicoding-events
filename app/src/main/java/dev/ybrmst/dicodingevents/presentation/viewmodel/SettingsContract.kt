package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.data.local.ThemeMode
import dev.ybrmst.dicodingevents.lib.ViewModelContract

class SettingsContract : ViewModelContract {

  @Immutable
  data class State(
    val themeMode: ThemeMode,
    val isOptInDailyNotif: Boolean,
  ) : ViewModelContract.State {
    companion object {
      fun initial() = State(
        themeMode = ThemeMode.SYSTEM,
        isOptInDailyNotif = false
      )
    }
  }

  sealed class Event : ViewModelContract.Event {
    data object OnFetch : Event()
    data class OnSetThemeMode(val darkMode: Boolean) : Event()
    data object OnToggleDailyNotifOptIn : Event()
  }

  sealed class Effect : ViewModelContract.Effect
}