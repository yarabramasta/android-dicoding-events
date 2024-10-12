package dev.ybrmst.dicoding_events

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.ybrmst.dicoding_events.ui.composables.screens.MainScreen
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppTheme {
        MainScreen()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
  AppTheme {
    MainScreen()
  }
}