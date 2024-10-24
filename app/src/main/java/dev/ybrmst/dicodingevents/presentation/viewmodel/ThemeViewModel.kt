package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.data.repositories.PreferenceRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
  private val repo: PreferenceRepositoryImpl,
) : ViewModel(),
  UnidirectionalViewModel<ThemeContract.State, ThemeContract.Event, ThemeContract.Effect> {

  private val mutableState = MutableStateFlow(ThemeContract.State.initial())
  override val state: StateFlow<ThemeContract.State>
    get() = mutableState.asStateFlow()

  private val effectFlow = MutableSharedFlow<ThemeContract.Effect>()
  override val effect: SharedFlow<ThemeContract.Effect>
    get() = effectFlow.asSharedFlow()

  init {
    viewModelScope.launch {
      val isDarkMode = repo.getDarkMode()
      mutableState.value = mutableState.value.copy(isDarkTheme = isDarkMode)
    }
  }

  override fun add(event: ThemeContract.Event) {
    when (event) {
      ThemeContract.Event.OnToggleDarkMode -> toggleDarkMode()
    }
  }

  private fun toggleDarkMode() {
    viewModelScope.launch {
      val isDarkMode = mutableState.value.isDarkTheme
      repo.setDarkMode(!isDarkMode)
      mutableState.value = mutableState.value.copy(isDarkTheme = !isDarkMode)
    }
  }
}