package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: EventsRepository,
) : BaseViewModel<
        HomeContract.State,
        HomeContract.Event,
        HomeContract.Effect>
  (initialState = HomeContract.State.initial()) {

  override fun add(event: HomeContract.Event) {
    when (event) {
      HomeContract.Event.OnFetch -> {
        setState { copy(isFetching = true, isRefreshing = false) }
        fetchEvents()
      }

      HomeContract.Event.OnRefresh -> {
        setState { copy(isRefreshing = true, isFetching = false) }
        fetchEvents()
      }

      is HomeContract.Event.OnFavoriteChanged -> {
        sendEffect(
          HomeContract.Effect.ShowToast(
            if (event.isFavorite) "Event removed from favorites"
            else "Event added to favorites"
          )
        )
      }
    }
  }

  init {
    add(HomeContract.Event.OnFetch)
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      repo.getUpcomingFinishedEvents().collect {
        it
          .onSuccess {
            setState {
              copy(
                isFetching = false,
                isRefreshing = false,
                upcomingEvents = data.first,
                finishedEvents = data.second
              )
            }
          }
          .onFailure {
            setState {
              copy(
                isFetching = false,
                isRefreshing = false,
                error = messageOrNull
                  ?: "Uh oh! Unexpected error occurred.\nPlease try again."
              )
            }
          }
      }
    }
  }
}