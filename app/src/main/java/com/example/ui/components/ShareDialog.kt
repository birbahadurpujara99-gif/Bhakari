package com.example.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.Post
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun ShareDialog(
  post: Post,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onNavigateToBluetoothShare: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val isNepali = currentLanguage == AppLanguage.NEPALI

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("share_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = LanguageStrings.get("share", currentLanguage),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Bluetooth Nearby Option
        ShareOptionRow(
          icon = Icons.Default.Bluetooth,
          iconBg = VerifiedGreen.copy(alpha = 0.15f),
          iconTint = VerifiedGreen,
          title = if (isNepali) "नजिकै ब्लुटुथबाट पठाउनुहोस्" else "Nearby Bluetooth Share",
          subtitle = if (isNepali) "इन्टरनेट बिना वरपरका साथीहरूलाई सिधै" else "Offline peer-to-peer device transfer",
          onClick = {
            onDismiss()
            onNavigateToBluetoothShare()
          },
          testTag = "share_via_bluetooth_option"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Copy Link Option
        ShareOptionRow(
          icon = Icons.Default.ContentCopy,
          iconBg = BhakariGold.copy(alpha = 0.15f),
          iconTint = BhakariGold,
          title = if (isNepali) "लिङ्क प्रतिलिपि गर्नुहोस्" else "Copy Link",
          subtitle = "https://bhakari.np/post/${post.id}",
          onClick = {
            clipboardManager.setText(AnnotatedString("https://bhakari.np/post/${post.id}"))
            Toast.makeText(
              context,
              if (isNepali) "लिङ्क प्रतिलिपि गरियो!" else "Link copied to clipboard!",
              Toast.LENGTH_SHORT
            ).show()
            onDismiss()
          },
          testTag = "copy_link_option"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Native Android Share Sheet
        ShareOptionRow(
          icon = Icons.Default.Share,
          iconBg = MaterialTheme.colorScheme.primaryContainer,
          iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
          title = if (isNepali) "अन्य एपहरूमा पठाउनुहोस्" else "Share to other apps",
          subtitle = if (isNepali) "WhatsApp, Viber, Messenger आदि" else "WhatsApp, Telegram, etc.",
          onClick = {
            val sendIntent: Intent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, "Bhakari Post by ${post.author.name}: ${post.content}\nhttps://bhakari.np/post/${post.id}")
              type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Bhakari Post")
            context.startActivity(shareIntent)
            onDismiss()
          },
          testTag = "share_external_option"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text(text = LanguageStrings.get("close", currentLanguage))
          }
        }
      }
    }
  }
}

@Composable
private fun ShareOptionRow(
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  testTag: String
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
          )
        )
      }
    }
  }
}
