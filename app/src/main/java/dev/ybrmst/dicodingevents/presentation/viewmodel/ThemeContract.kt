package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable

interface ThemeContract {
  @Immutable
  data class State(val isDarkTheme: Boolean) {
    companion object {
      fun initial() = State(isDarkTheme = false)
    }
  }

  sealed class Event {
    data object OnToggleDarkMode : Event()
  }

  sealed class Effect
}