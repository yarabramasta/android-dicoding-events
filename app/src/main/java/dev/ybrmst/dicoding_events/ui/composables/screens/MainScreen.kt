package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MainScreen(
  navController: NavHostController,
) {

  Scaffold { innerPadding ->
    HomeScreen(
      modifier = Modifier.padding(innerPadding),
      navController = navController
    )
  }
}