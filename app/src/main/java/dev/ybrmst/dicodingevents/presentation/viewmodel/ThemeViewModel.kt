package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.data.repositories.PreferenceRepositoryImpl
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
  private val repo: PreferenceRepositoryImpl,
) :
  BaseViewModel<
          ThemeContract.State,
          ThemeContract.Event,
          ThemeContract.Effect>
    (initialState = ThemeContract.State.initial()) {

  init {
    viewModelScope.launch {
      val isDarkMode = repo.getDarkMode()
      setState { copy(isDarkTheme = isDarkMode) }
    }
  }

  override fun add(event: ThemeContract.Event) {
    when (event) {
      ThemeContract.Event.OnToggleDarkMode -> toggleDarkMode()
    }
  }

  private fun toggleDarkMode() {
    viewModelScope.launch {
      val isDarkMode = state.value.isDarkTheme
      repo.setDarkMode(!isDarkMode)

      setState { copy(isDarkTheme = !isDarkMode) }
    }
  }
}