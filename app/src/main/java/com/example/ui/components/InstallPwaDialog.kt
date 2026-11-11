package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariNavy
import com.example.ui.theme.VerifiedGreen

@Composable
fun InstallPwaDialog(
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onInstallConfirmed: () -> Unit
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("install_pwa_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // App Logo Icon with Glow
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(76.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.radialGradient(
                listOf(BhakariGold.copy(alpha = 0.3f), Color.Transparent)
              )
            )
            .border(2.dp, BhakariGold, RoundedCornerShape(20.dp))
        ) {
          Image(
            painter = painterResource(id = R.drawable.bhakari_logo),
            contentDescription = "Bhakari App Logo",
            modifier = Modifier.size(68.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title
        Text(
          text = LanguageStrings.get("pwa_dialog_title", currentLanguage),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          ),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Chrome PWA Subtitle
        Text(
          text = if (isNepali)
            "गुगल क्रोम (Google Chrome) बाट सिधै PWA एपको रूपमा इन्स्टल गर्न मिल्ने"
          else
            "Installable directly from Google Chrome as a PWA",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = VerifiedGreen,
            fontWeight = FontWeight.SemiBold
          ),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Features List Cards
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          PwaFeatureRow(
            icon = Icons.Default.ElectricBolt,
            title = if (isNepali) "द्रुत गति र हलुका एप" else "Fast & Instant Launch",
            description = if (isNepali) "कुनै पनि डिभाइसमा छरितो चल्ने" else "Loads instantly with cached storage"
          )
          PwaFeatureRow(
            icon = Icons.Default.Bluetooth,
            title = if (isNepali) "अफलाइन ब्लुटुथ सेयरिङ" else "Offline Bluetooth Sharing",
            description = if (isNepali) "इन्टरनेट बिना पोस्ट र सम्पर्क साटासाट" else "Peer-to-peer data sharing nearby"
          )
          PwaFeatureRow(
            icon = Icons.Default.Home,
            title = if (isNepali) "होम स्क्रिन सर्टकट" else "Home Screen Direct Access",
            description = if (isNepali) "मोबाइलको स्क्रिनबाट १-ट्यापमा खोल्नुहोस्" else "Launches full-screen like native app"
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Chrome Step Instructions Card
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.OpenInBrowser,
              contentDescription = "Chrome Menu",
              tint = BhakariGold,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = LanguageStrings.get("pwa_chrome_step", currentLanguage),
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons: 1) Open in Android Chrome (Triggers Browser Prompt) 2) Add PWA Shortcut
        Button(
          onClick = {
            try {
              val pwaUri = android.net.Uri.parse("https://ais-dev-6tryqtllcmrxdap6uhyf64-836283227085.asia-northeast1.run.app")
              val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, pwaUri).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                // Prefer Google Chrome if installed
                setPackage("com.android.chrome")
              }
              try {
                context.startActivity(intent)
              } catch (e: Exception) {
                // Fallback to default browser
                val fallbackIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, pwaUri).apply {
                  addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
              }
            } catch (err: Exception) {
              Toast.makeText(context, "Opening PWA in Browser...", Toast.LENGTH_SHORT).show()
            }
            onInstallConfirmed()
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("chrome_pwa_launch_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = VerifiedGreen
          )
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isNepali) "क्रोममा PWA इन्स्टल गर्नुहोस् (Chrome Install)" else "Install on Android Chrome",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Direct Download APK Button
        Button(
          onClick = {
            try {
              val apkUri = android.net.Uri.parse("https://ais-dev-6tryqtllcmrxdap6uhyf64-836283227085.asia-northeast1.run.app/bhakari.apk")
              val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, apkUri)
              context.startActivity(intent)
              Toast.makeText(context, "Downloading Bhakari APK...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
              Toast.makeText(context, "Export via AI Studio Settings menu", Toast.LENGTH_SHORT).show()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("download_apk_dialog_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = BhakariGold
          )
        ) {
          Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isNepali) "एन्ड्रोइड APK डाउनलोड गर्नुहोस् (२८ MB)" else "Download Android APK (28 MB)",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Native Shortcut Button
        OutlinedButton(
          onClick = {
            onInstallConfirmed()
            Toast.makeText(
              context,
              LanguageStrings.get("pwa_installed_toast", currentLanguage),
              Toast.LENGTH_LONG
            ).show()
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("pin_shortcut_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Home,
            contentDescription = null,
            tint = BhakariGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = LanguageStrings.get("pwa_install_now", currentLanguage),
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onDismiss,
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = LanguageStrings.get("close", currentLanguage),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
private fun PwaFeatureRow(
  icon: ImageVector,
  title: String,
  description: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(VerifiedGreen.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = VerifiedGreen,
        modifier = Modifier.size(20.dp)
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
        text = description,
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      )
    }
  }
}
