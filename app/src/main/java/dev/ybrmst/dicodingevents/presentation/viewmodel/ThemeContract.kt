package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import dev.ybrmst.dicodingevents.lib.ViewModelContract

interface ThemeContract : ViewModelContract {
  @Immutable
  data class State(val isDarkTheme: Boolean) : ViewModelContract.State {
    companion object {
      fun initial() = State(isDarkTheme = false)
    }
  }

  sealed class Event : ViewModelContract.Event {
    data object OnToggleDarkMode : Event()
  }

  sealed class Effect : ViewModelContract.Effect
}