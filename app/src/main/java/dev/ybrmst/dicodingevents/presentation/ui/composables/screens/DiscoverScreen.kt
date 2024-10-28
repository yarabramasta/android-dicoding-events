package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.TabItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.TabView
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberUpdatedFavoriteStatus
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel

@Composable
fun DiscoverScreen(
  modifier: Modifier = Modifier,
  vm: DiscoverViewModel = hiltViewModel(),
  favsVm: FavoritesViewModel,
  onEventClick: (EventPreview) -> Unit = {},
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is DiscoverContract.Effect.ShowToast -> {
          Toast.makeText(
            context,
            action.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  val favs by favsVm.state.collectAsStateWithLifecycle()
  val upcomingEvents = rememberUpdatedFavoriteStatus(
    state.upcomingEvents,
    favs.events
  )
  val finishedEvents = rememberUpdatedFavoriteStatus(
    state.finishedEvents,
    favs.events
  )

  val tabItems = listOf(
    TabItem(
      title = "Upcoming",
      icon = Icons.Outlined.EventAvailable,
      selectedIcon = Icons.Filled.EventAvailable
    ),
    TabItem(
      title = "Finished",
      icon = Icons.Outlined.EventBusy,
      selectedIcon = Icons.Filled.EventBusy
    ),
  )

  TabView(
    modifier = modifier.fillMaxSize(),
    items = tabItems
  ) { activePage ->
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
      verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
      Text("Active page: $activePage")
    }
  }
}