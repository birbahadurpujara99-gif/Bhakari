package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.User
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun UserProfileScreen(
  currentUser: User?,
  ceoUser: User,
  currentLanguage: AppLanguage,
  onToggleLanguage: () -> Unit,
  onOpenCeoProfile: () -> Unit,
  onOpenInstallDialog: () -> Unit,
  onSwitchToCeo: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val user = currentUser ?: ceoUser

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .testTag("user_profile_screen"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
      item {
        Text(
          text = LanguageStrings.get("nav_profile", currentLanguage),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Overview Card
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            UserAvatar(
              user = user,
              size = 80.dp,
              showBorder = user.isCeo,
              showVerifiedBadge = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isNepali) user.nepaliName else user.name,
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              if (user.isCeo) {
                Spacer(modifier = Modifier.width(6.dp))
                VerifiedBadge(size = 18.dp)
              }
            }

            Text(
              text = "@${user.username}",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )

            if (user.isCeo) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "CEO & Founder of Bhakari 🌾🇳🇵",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = VerifiedGreen,
                  fontWeight = FontWeight.Bold
                )
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = if (isNepali && user.nepaliBio.isNotBlank()) user.nepaliBio else user.bio,
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
              ),
              modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // User Info Details (Phone, Email)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              if (user.phone.isNotBlank()) {
                InfoPill(icon = Icons.Default.Phone, text = user.phone)
              }
              if (user.email.isNotBlank()) {
                InfoPill(icon = Icons.Default.Email, text = user.email)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CEO Bir Pujara Dedicated Profile Banner
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = VerifiedGreen.copy(alpha = 0.12f)
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(VerifiedGreen, BhakariGold))
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenCeoProfile() }
            .testTag("view_ceo_profile_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(2.dp, VerifiedGreen, CircleShape)
            ) {
              Image(
                painter = painterResource(id = R.drawable.ceo_bir_pujara),
                contentDescription = "CEO Bir Pujara",
                modifier = Modifier.size(46.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isNepali) "सिईओ प्रोफाइल हेर्नुहोस् (वीर पुजारा)" else "View CEO Profile (Bir Pujara)",
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )
                Spacer(modifier = Modifier.width(4.dp))
                VerifiedBadge(size = 14.dp)
              }
              Text(
                text = if (isNepali) "हरियो प्रमाणित टिक र आधिकारिक अपडेटहरू" else "Green verified tick & official announcements",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = VerifiedGreen,
                  fontSize = 11.sp
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Settings / Quick Actions List
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(8.dp)) {
            // Language Toggle Option
            ProfileSettingRow(
              icon = Icons.Default.Language,
              iconTint = BhakariGold,
              title = if (isNepali) "भाषा: नेपाली (English मा बदल्नुहोस्)" else "Language: English (Switch to नेपाली)",
              subtitle = "Instant bilingual toggle",
              onClick = onToggleLanguage,
              testTag = "profile_language_toggle"
            )

            // Install PWA Option
            ProfileSettingRow(
              icon = Icons.Default.Download,
              iconTint = VerifiedGreen,
              title = LanguageStrings.get("pwa_dialog_title", currentLanguage),
              subtitle = LanguageStrings.get("pwa_banner_subtitle", currentLanguage),
              onClick = onOpenInstallDialog,
              testTag = "profile_install_pwa"
            )

            // Switch account / Demo as CEO
            if (!user.isCeo) {
              ProfileSettingRow(
                icon = Icons.Default.Star,
                iconTint = BhakariGold,
                title = if (isNepali) "सिईओ खातामा स्विच गर्नुहोस्" else "Switch to CEO Account",
                subtitle = "Demo official CEO Bir Pujara account with verified badge",
                onClick = onSwitchToCeo,
                testTag = "switch_to_ceo_button"
              )
            }

            // Logout
            ProfileSettingRow(
              icon = Icons.Default.ExitToApp,
              iconTint = BhakariCrimson,
              title = LanguageStrings.get("logout", currentLanguage),
              subtitle = "Sign out from this device",
              onClick = onLogout,
              testTag = "logout_button"
            )
          }
        }

        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
private fun InfoPill(icon: ImageVector, text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = BhakariGold,
      modifier = Modifier.size(14.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
    )
  }
}

@Composable
private fun ProfileSettingRow(
  icon: ImageVector,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 10.dp)
      .testTag(testTag),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(iconTint.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(18.dp)
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold,
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
