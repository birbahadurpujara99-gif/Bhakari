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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.User
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.FacebookVerifiedBlue
import com.example.ui.theme.VerifiedGreen

@Composable
fun UserProfileScreen(
  currentUser: User?,
  ceoUser: User,
  currentLanguage: AppLanguage,
  onToggleLanguage: () -> Unit,
  onOpenCeoProfile: () -> Unit,
  onOpenInstallDialog: () -> Unit,
  onOpenVerificationRequest: () -> Unit,
  onOpenAdminDashboard: () -> Unit,
  onOpenEditProfile: () -> Unit,
  onOpenVerificationInfo: (User) -> Unit,
  onSwitchToCeo: () -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val isHindi = currentLanguage == AppLanguage.HINDI
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
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = LanguageStrings.get("nav_profile", currentLanguage),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
          OutlinedButton(
            onClick = onOpenEditProfile,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.testTag("header_edit_profile_btn")
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(LanguageStrings.get("edit_profile", currentLanguage), fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Overview Card with Cover Photo Banner
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            // Cover Photo Banner
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(FacebookVerifiedBlue.copy(alpha = 0.2f))
            ) {
              if (user.coverUrl.isNotBlank()) {
                AsyncImage(
                  model = user.coverUrl,
                  contentDescription = "Cover",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              } else {
                Image(
                  painter = painterResource(id = R.drawable.bhakari_cover_art),
                  contentDescription = "Default cover",
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
              }
            }

            // Avatar & User Details
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              UserAvatar(
                user = user,
                size = 80.dp,
                showBorder = user.isCeo || user.hasBlueBadge,
                showVerifiedBadge = true,
                onBadgeClick = { onOpenVerificationInfo(user) }
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isNepali) user.nepaliName else if (isHindi && user.hindiName.isNotBlank()) user.hindiName else user.name,
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )
                if (user.hasBlueBadge) {
                  Spacer(modifier = Modifier.width(6.dp))
                  VerifiedBadge(
                    size = 18.dp,
                    isCeo = user.isCeo,
                    onClick = { onOpenVerificationInfo(user) }
                  )
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
                  text = "Founder & CEO of Bhakari 🌾🇳🇵",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = FacebookVerifiedBlue,
                    fontWeight = FontWeight.Bold
                  )
                )
              } else if (user.title.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = if (isNepali && user.nepaliTitle.isNotBlank()) user.nepaliTitle else user.title,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
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

              // Phone, Email Info Pills
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
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CEO Bir Pujara Dedicated Profile Banner (if viewing as normal user)
        if (!user.isCeo) {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = FacebookVerifiedBlue.copy(alpha = 0.08f)),
            border = CardDefaults.outlinedCardBorder().copy(
              brush = Brush.horizontalGradient(listOf(FacebookVerifiedBlue, BhakariGold))
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
                  .border(2.dp, FacebookVerifiedBlue, CircleShape)
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
                    text = "Bir Pujara (CEO)",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  VerifiedBadge(size = 14.dp, isCeo = true, onClick = { onOpenVerificationInfo(ceoUser) })
                }
                Text(
                  text = "Founder & CEO, Bhakari • Permanent Blue Verified Badge",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = FacebookVerifiedBlue,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
        }

        // CEO Admin Dashboard Card (Exclusively for CEO Bir Pujara)
        if (user.isCeo || user.role == "ceo") {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = FacebookVerifiedBlue.copy(alpha = 0.12f)),
            border = CardDefaults.outlinedCardBorder().copy(
              brush = Brush.horizontalGradient(listOf(FacebookVerifiedBlue, BhakariGold))
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenAdminDashboard() }
              .testTag("ceo_admin_dashboard_card")
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
                  .background(FacebookVerifiedBlue),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = LanguageStrings.get("admin_dashboard", currentLanguage),
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  VerifiedBadge(size = 14.dp, isCeo = true)
                }
                Text(
                  text = LanguageStrings.get("admin_desc", currentLanguage),
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = FacebookVerifiedBlue,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
        }

        // Settings / Quick Actions List
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(8.dp)) {
            // Edit Profile Row
            ProfileSettingRow(
              icon = Icons.Default.Edit,
              iconTint = FacebookVerifiedBlue,
              title = LanguageStrings.get("edit_profile", currentLanguage),
              subtitle = "Change photo, cover, name, bio & privacy settings",
              onClick = onOpenEditProfile,
              testTag = "profile_edit_row"
            )

            // Apply for Blue Verification Badge (User Settings)
            if (!user.isCeo) {
              ProfileSettingRow(
                icon = Icons.Default.Verified,
                iconTint = FacebookVerifiedBlue,
                title = if (user.hasBlueBadge) {
                  "You have Blue Verified Badge (🔵✓)"
                } else {
                  LanguageStrings.get("request_verification", currentLanguage)
                },
                subtitle = if (user.hasBlueBadge) {
                  "Verified authentic account confirmed by CEO Bir Pujara"
                } else {
                  LanguageStrings.get("request_verification_sub", currentLanguage)
                },
                onClick = onOpenVerificationRequest,
                testTag = "profile_request_verification"
              )
            }

            // Language Toggle Option (Trilingual: English, Nepali, Hindi)
            ProfileSettingRow(
              icon = Icons.Default.Language,
              iconTint = BhakariGold,
              title = "Language: ${currentLanguage.displayName} (${currentLanguage.flag})",
              subtitle = "Switch to ${currentLanguage.next().displayName}",
              onClick = onToggleLanguage,
              testTag = "profile_language_toggle"
            )

            // Install PWA Option
            ProfileSettingRow(
              icon = Icons.Default.Download,
              iconTint = VerifiedGreen,
              title = "Install Bhakari App (PWA)",
              subtitle = "Fast instant launch on Android Home Screen",
              onClick = onOpenInstallDialog,
              testTag = "profile_install_pwa"
            )

            // Switch account / Demo as CEO
            if (!user.isCeo) {
              ProfileSettingRow(
                icon = Icons.Default.Star,
                iconTint = BhakariGold,
                title = "Switch to CEO Account (Bir Pujara)",
                subtitle = "Access CEO Admin Dashboard and verified badge controls",
                onClick = onSwitchToCeo,
                testTag = "switch_to_ceo_button"
              )
            }

            // Logout
            ProfileSettingRow(
              icon = Icons.Default.ExitToApp,
              iconTint = BhakariCrimson,
              title = LanguageStrings.get("logout", currentLanguage),
              subtitle = "Sign out securely from this device",
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
      tint = FacebookVerifiedBlue,
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
