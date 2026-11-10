package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariNavy
import com.example.ui.theme.VerifiedGreen

@Composable
fun BhakariTopBar(
  currentLanguage: AppLanguage,
  onToggleLanguage: () -> Unit,
  onOpenInstallDialog: () -> Unit,
  onOpenCeoProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // App Logo & Brand Title
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onOpenCeoProfile() }
            .testTag("app_branding_clickable")
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .border(1.5.dp, BhakariGold, RoundedCornerShape(10.dp))
          ) {
            Image(
              painter = painterResource(id = R.drawable.bhakari_logo),
              contentDescription = "Bhakari App Logo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Bhakari",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = BhakariGold,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = " | भकारी",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BhakariCrimson
                )
              )
            }
            Text(
              text = if (isNepali) "विश्वभरका नेपालीहरूको सञ्जाल" else "Connecting Nepalis Worldwide",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
              )
            )
          }
        }

        // Action Buttons: Language Switch & Install App
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Language Switch Pill Button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(BhakariGold.copy(alpha = 0.2f), BhakariCrimson.copy(alpha = 0.2f))
                )
              )
              .border(
                width = 1.dp,
                color = BhakariGold.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp)
              )
              .clickable { onToggleLanguage() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("language_switch_button"),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isNepali) "🇳🇵 नेपाली" else "🇬🇧 English",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontSize = 12.sp
                )
              )
            }
          }

          // Install App (PWA) Button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(VerifiedGreen, Color(0xFF047857))
                )
              )
              .clickable { onOpenInstallDialog() }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("install_app_button"),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Install App",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = LanguageStrings.get("install_app_btn", currentLanguage),
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 11.sp
                )
              )
            }
          }
        }
      }
    }
  }
}
