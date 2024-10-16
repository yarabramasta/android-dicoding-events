package dev.ybrmst.dicoding_events.ui.composable.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import dev.ybrmst.dicoding_events.ui.composable.nav.BottomNavBar
import dev.ybrmst.dicoding_events.ui.composable.nav.BottomNavItem

@Composable
fun MainScreen(
  navController: NavHostController,
) {

  MainScreenScaffold { innerPadding, activeScreen ->
    when (activeScreen) {
      "Home" -> {
        HomeScreen(
          navController = navController,
          modifier = Modifier.padding(innerPadding)
        )
      }

      "Upcoming" -> {
        UpcomingEventsScreen(
          navController = navController,
          modifier = Modifier.padding(innerPadding)
        )
      }

      "Finished" -> {
        FinishedEventsScreen(
          navController = navController,
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}

@Composable
fun MainScreenScaffold(
  content: @Composable (
    padding: PaddingValues,
    activeScreen: String,
  ) -> Unit,
) {
  val items = listOf(
    BottomNavItem(
      label = "Home",
      selectedIcon = Icons.Filled.Home,
      unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
      label = "Upcoming",
      selectedIcon = Icons.Filled.Upcoming,
      unselectedIcon = Icons.Outlined.Upcoming,
    ),
    BottomNavItem(
      label = "Finished",
      selectedIcon = Icons.Filled.Category,
      unselectedIcon = Icons.Outlined.Category,
    ),
  )

  var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

  Scaffold(
    bottomBar = {
      BottomNavBar(items = items,
        selectedItemIndex = selectedItemIndex,
        onItemSelected = { selectedItemIndex = it })
    }
  ) {
    content(
      it,
      items[selectedItemIndex].label,
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  MainScreenScaffold { innerPadding, activeScreen ->
    Text("$activeScreen Screen", modifier = Modifier.padding(innerPadding))
  }
}