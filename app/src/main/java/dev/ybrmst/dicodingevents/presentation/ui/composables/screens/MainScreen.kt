package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.BottomNavItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.MainScreenBottomNavBar
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.Placeholder
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.HomeContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.HomeViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsViewModel

@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  homeVm: HomeViewModel = hiltViewModel(),
  discoverVm: DiscoverViewModel = hiltViewModel(),
  favsVm: FavoritesViewModel = hiltViewModel(),
  settingsVm: SettingsViewModel = hiltViewModel(),
) {
  MainScreenContent(
    modifier = modifier.fillMaxSize(),
    onItemSelected = { index ->
      when (index) {
        0 -> homeVm.add(HomeContract.Event.OnScreenChanged)
        1 -> discoverVm.add(DiscoverContract.Event.OnScreenChanged)
        2 -> favsVm.add(FavoritesContract.Event.OnScreenChanged)
      }
    }
  ) { activeScreen, innerPadding ->
    when (activeScreen) {
      "Home" -> HomeScreen(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        vm = homeVm,
      )

      "Discover" -> Placeholder(
        modifier = Modifier.padding(innerPadding)
      )

      "Favorites" -> FavoritesScreen(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        vm = favsVm,
      )

      "Settings" -> SettingsScreen(
        modifier = Modifier.padding(innerPadding),
        vm = settingsVm,
      )
    }
  }
}

@Composable
private fun MainScreenContent(
  modifier: Modifier = Modifier,
  onItemSelected: (Int) -> Unit = {},
  content: @Composable (
    activeScreen: String,
    paddingValues: PaddingValues,
  ) -> Unit,
) {
  val bottomNavItems = listOf(
    BottomNavItem(
      label = "Home",
      selectedIcon = Icons.Filled.Home,
      icon = Icons.Outlined.Home
    ),
    BottomNavItem(
      label = "Discover",
      selectedIcon = Icons.Filled.Explore,
      icon = Icons.Outlined.Explore
    ),
    BottomNavItem(
      label = "Favorites",
      selectedIcon = Icons.Filled.Star,
      icon = Icons.Outlined.StarOutline
    ),
    BottomNavItem(
      label = "Settings",
      selectedIcon = Icons.Filled.Settings,
      icon = Icons.Outlined.Settings
    )
  )

  var currentActiveIndex by rememberSaveable { mutableIntStateOf(0) }

  Scaffold(
    modifier = modifier,
    bottomBar = {
      MainScreenBottomNavBar(
        items = bottomNavItems,
        selectedItemIndex = currentActiveIndex,
        onItemSelected = { index ->
          currentActiveIndex = index
          onItemSelected(index)
        }
      )
    }
  ) { innerPadding ->
    content(
      bottomNavItems[currentActiveIndex].label,
      innerPadding,
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenPreview() {
  AppTheme {
    MainScreenContent { activeScreen, paddingValues ->
      Placeholder(
        label = "Current Screen: $activeScreen",
        modifier = Modifier.padding(paddingValues)
      )
    }
  }
}