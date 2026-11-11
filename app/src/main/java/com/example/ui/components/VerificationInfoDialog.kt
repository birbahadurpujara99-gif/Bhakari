package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.User
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.FacebookVerifiedBlue

/**
 * Information dialog shown when clicking any blue verification badge.
 * - Displays "CEO of Bhakari" for CEO Bir Pujara.
 * - Displays "Verified Account" for authenticated public figures & creators.
 */
@Composable
fun VerificationInfoDialog(
  user: User,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val isHindi = currentLanguage == AppLanguage.HINDI
  val isCeo = user.isCeo || user.username == "birpujara_ceo" || user.id == "ceo_bir_pujara"

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .testTag("verification_info_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top Close Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        // Prominent Blue Verified Badge Icon
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(FacebookVerifiedBlue.copy(alpha = 0.12f))
        ) {
          VerifiedBadge(size = 46.dp, isCeo = isCeo)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title: "CEO of Bhakari" or "Verified Account"
        Text(
          text = if (isCeo) {
            LanguageStrings.get("ceo_badge_info_title", currentLanguage)
          } else {
            LanguageStrings.get("blue_badge_info_title", currentLanguage)
          },
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Name & Username
        Text(
          text = if (isNepali) user.nepaliName else if (isHindi && user.hindiName.isNotBlank()) user.hindiName else user.name,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold,
            color = FacebookVerifiedBlue
          )
        )
        Text(
          text = "@${user.username}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Verification Details Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = FacebookVerifiedBlue.copy(alpha = 0.08f)
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(FacebookVerifiedBlue.copy(alpha = 0.3f))
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = FacebookVerifiedBlue,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isCeo) {
                  LanguageStrings.get("ceo_badge_info_desc", currentLanguage)
                } else {
                  LanguageStrings.get("blue_badge_info_desc", currentLanguage)
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = MaterialTheme.colorScheme.onSurface,
                  lineHeight = 20.sp
                )
              )
              if (isCeo) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "✓ Founder & CEO of Bhakari\n✓ Full Administrative Authority\n✓ Permanent Blue Verified Badge",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = FacebookVerifiedBlue,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("close_verification_info_btn")
        ) {
          Text(
            text = LanguageStrings.get("close", currentLanguage),
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
