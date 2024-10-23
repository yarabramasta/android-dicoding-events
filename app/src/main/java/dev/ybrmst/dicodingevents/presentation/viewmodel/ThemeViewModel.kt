package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.data.local.PreferenceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ThemeState(val isDarkTheme: Boolean)

@HiltViewModel
class ThemeViewModel @Inject constructor(
  private val preferenceService: PreferenceService,
) : ViewModel() {
  private val _state = MutableStateFlow(ThemeState(isDarkTheme = false))
  val state: StateFlow<ThemeState>
    get() = _state.asStateFlow()

  init {
    viewModelScope.launch {
      val isDarkMode = preferenceService.getDarkMode()
      _state.tryEmit(ThemeState(isDarkTheme = isDarkMode))
    }
  }

  fun toggleDarkMode() {
    viewModelScope.launch {
      val isDarkMode = _state.value.isDarkTheme
      preferenceService.setDarkMode(!isDarkMode)
      _state.tryEmit(ThemeState(isDarkTheme = !isDarkMode))
    }
  }
}