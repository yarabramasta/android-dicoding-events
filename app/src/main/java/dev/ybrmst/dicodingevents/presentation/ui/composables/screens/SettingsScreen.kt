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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
  SettingsScreenContent(
    modifier = modifier,
    isDarkMode = false,
    onDarkModeChanged = {},
    isOptInDailyNotif = false,
    onDailyNotifChanged = {}
  )
}

@Composable
fun SettingsScreenContent(
  modifier: Modifier = Modifier,
  isDarkMode: Boolean,
  onDarkModeChanged: (Boolean) -> Unit,
  isOptInDailyNotif: Boolean,
  onDailyNotifChanged: (Boolean) -> Unit,
) {
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
            if (isDarkMode) Icons.Outlined.DarkMode
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
            checked = isDarkMode,
            onCheckedChange = onDarkModeChanged
          )
        }
      )
      ListItem(
        leadingContent = {
          Icon(
            if (isOptInDailyNotif) Icons.Outlined.NotificationsActive
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
            checked = isOptInDailyNotif,
            onCheckedChange = onDailyNotifChanged
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
        onDarkModeChanged = {},
        isOptInDailyNotif = false,
        onDailyNotifChanged = {}
      )
    }
  }
}