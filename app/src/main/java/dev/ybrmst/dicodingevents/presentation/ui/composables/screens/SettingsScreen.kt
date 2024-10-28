package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ybrmst.dicodingevents.presentation.ui.composables.isDarkThemeEnabled
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
  modifier: Modifier = Modifier,
  vm: SettingsViewModel = hiltViewModel(),
) {
  val settings by vm.state.collectAsState()
  val isDarkMode = isDarkThemeEnabled(settings.themeMode)

  SettingsScreenContent(
    modifier = modifier,
    isDarkMode = isDarkMode,
    onChangeDarkMode = {
      vm.add(SettingsContract.Event.OnSetThemeMode(it))
    },
    isOptInDailyNotif = settings.isOptInDailyNotif,
    onToggleOptInDailyNotif = {
      vm.add(SettingsContract.Event.OnToggleDailyNotifOptIn)
    }
  )
}

@Composable
fun SettingsScreenContent(
  modifier: Modifier = Modifier,
  isDarkMode: Boolean,
  onChangeDarkMode: (Boolean) -> Unit,
  isOptInDailyNotif: Boolean,
  onToggleOptInDailyNotif: () -> Unit,
) {
  var darkMode by remember(isDarkMode) { mutableStateOf(isDarkMode) }
  var dailyNotifOptIn by remember(isOptInDailyNotif) { mutableStateOf(isOptInDailyNotif) }

  LazyColumn(modifier = modifier.fillMaxSize()) {
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        "Settings",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        "Customize to your preference.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
      Spacer(modifier = Modifier.height(24.dp))
      ListItem(
        leadingContent = {
          Icon(
            if (darkMode) Icons.Outlined.DarkMode
            else Icons.Outlined.LightMode,
            contentDescription = null
          )
        },
        headlineContent = {
          Text(
            "Dark Mode",
            modifier = Modifier.padding(bottom = 4.dp)
          )
        },
        supportingContent = {
          Text(
            "Toggle application dark theme.",
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        trailingContent = {
          Switch(
            checked = darkMode,
            onCheckedChange = {
              onChangeDarkMode(it)
//              darkMode = it
            }
          )
        }
      )
      ListItem(
        leadingContent = {
          Icon(
            if (dailyNotifOptIn) Icons.Outlined.NotificationsActive
            else Icons.Outlined.Notifications,
            contentDescription = null
          )
        },
        headlineContent = {
          Text(
            "Daily Notification",
            modifier = Modifier.padding(bottom = 4.dp)
          )
        },
        supportingContent = {
          Text(
            "Get notification for upcoming events.",
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        trailingContent = {
          Switch(
            checked = dailyNotifOptIn,
            onCheckedChange = {
              onToggleOptInDailyNotif()
//              dailyNotifOptIn = it
            }
          )
        }
      )
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSettingsScreen() {
  AppTheme {
    Scaffold { innerPadding ->
      SettingsScreenContent(
        modifier = Modifier.padding(innerPadding),
        isDarkMode = true,
        onChangeDarkMode = {},
        isOptInDailyNotif = false,
        onToggleOptInDailyNotif = {}
      )
    }
  }
}