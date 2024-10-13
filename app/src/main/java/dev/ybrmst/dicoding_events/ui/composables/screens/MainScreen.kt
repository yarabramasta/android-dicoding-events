package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {

  Scaffold { innerPadding ->
    HomeScreen(modifier = Modifier.padding(innerPadding))
  }
}