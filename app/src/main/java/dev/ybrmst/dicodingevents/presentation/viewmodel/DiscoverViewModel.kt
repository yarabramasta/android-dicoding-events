package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val repo: EventsRepository,
) :
  BaseViewModel<
          DiscoverContract.State,
          DiscoverContract.Event,
          DiscoverContract.Effect>
    (initialState = DiscoverContract.State.initial()) {

  private var searchJob: Job? = null

  override fun add(event: DiscoverContract.Event) {
    when (event) {
      is DiscoverContract.Event.OnFetching -> {
        updateStateForCategory(
          category = event.category,
          isFetching = true
        )
        fetchEvents(category = event.category)
      }

      is DiscoverContract.Event.OnRefreshing -> {
        updateStateForCategory(
          category = event.category,
          isRefreshing = true
        )
        fetchEvents(category = event.category)
      }

      is DiscoverContract.Event.OnSearchQueryChanged -> {
        if (event.query.isBlank()) {
          updateStateForCategory(
            category = event.category,
            isSearching = false
          )
          fetchEvents(category = event.category)
        } else {
          updateStateForCategory(
            category = event.category,
            isSearching = true,
            searchQuery = event.query
          )
          searchJob?.cancel()
          searchJob = viewModelScope.launch {
            delay(500L)
            fetchEvents(event.query, event.category)
          }
        }
      }

      is DiscoverContract.Event.OnEventFavoriteChanged -> {
        toggleFavorite(event.event, event.category)
      }
    }
  }

  private fun fetchEvents(
    searchQuery: String? = null,
    category: DiscoverContract.EventCategory,
  ) {
    viewModelScope.launch {
      repo.queryEvents(
        query = searchQuery,
        active = when (category) {
          DiscoverContract.EventCategory.UPCOMING -> 1
          DiscoverContract.EventCategory.FINISHED -> 0
        }
      ).collect {
        it
          .onSuccess {
            updateStateForCategory(
              category = category,
              isFetching = false,
              isRefreshing = false,
              isSearching = false,
              events = data
            )
          }
          .onFailure {
            updateStateForCategory(
              category = category,
              error = messageOrNull
                ?: "Uh oh! Unexpected error occurred.\nPlease try again."
            )
          }
      }
    }
  }

  private fun toggleFavorite(
    event: EventPreview,
    category: DiscoverContract.EventCategory,
  ) {
    viewModelScope.launch {
      val (updatedEvent, error) = if (event.isFavorite) {
        repo.removeFavEvent(event)
      } else {
        repo.addFavEvent(event)
      }

      if (error != null) {
        setState {
          copy(
            errorUpcoming = error.message
              ?: "Uh oh! Unexpected error occurred.\nPlease try again."
          )
        }
      } else {
        val updatedEvents = when (category) {
          DiscoverContract.EventCategory.UPCOMING -> state.value.upcomingEvents
          DiscoverContract.EventCategory.FINISHED -> state.value.finishedEvents
        }.map {
          if (it.id == updatedEvent.id) updatedEvent else it
        }

        updateStateForCategory(
          category = category,
          events = updatedEvents
        )
      }

      sendEffect(
        DiscoverContract.Effect.ShowToast(
          if (updatedEvent.isFavorite) "Event added to favorites."
          else "Event removed from favorites."
        )
      )
    }
  }

  private fun updateStateForCategory(
    category: DiscoverContract.EventCategory,
    isFetching: Boolean? = null,
    isRefreshing: Boolean? = null,
    isSearching: Boolean? = null,
    searchQuery: String? = null,
    events: List<EventPreview>? = null,
    error: String? = null,
  ) {
    setState {
      when (category) {
        DiscoverContract.EventCategory.UPCOMING -> copy(
          isFetchingUpcoming = isFetching ?: this.isFetchingUpcoming,
          isRefreshingUpcoming = isRefreshing ?: this.isRefreshingUpcoming,
          isSearchingUpcoming = isSearching ?: this.isSearchingUpcoming,
          searchQueryUpcoming = searchQuery ?: this.searchQueryUpcoming,
          upcomingEvents = events ?: this.upcomingEvents,
          errorUpcoming = error
        )

        DiscoverContract.EventCategory.FINISHED -> copy(
          isFetchingFinished = isFetching ?: this.isFetchingFinished,
          isRefreshingFinished = isRefreshing ?: this.isRefreshingFinished,
          isSearchingFinished = isSearching ?: this.isSearchingFinished,
          searchQueryFinished = searchQuery ?: this.searchQueryFinished,
          finishedEvents = events ?: this.finishedEvents,
          errorFinished = error
        )
      }
    }
  }
}