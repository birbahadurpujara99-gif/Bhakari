package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FacebookVerifiedBlue

/**
 * Facebook-style circular blue verification badge with a crisp white checkmark (🔵✓).
 * - Displayed next to verified users' names and profile pictures.
 * - CEO Bir Pujara receives a permanent blue verification badge with optional CEO title indicator.
 * - Interactive: Clicking the badge triggers onClick to display official verification info dialog.
 */
@Composable
fun VerifiedBadge(
  modifier: Modifier = Modifier,
  size: Dp = 15.dp,
  isCeo: Boolean = false,
  showLabel: Boolean = false,
  labelText: String = if (isCeo) "CEO" else "Verified",
  onClick: (() -> Unit)? = null
) {
  // Classic Facebook verified blue #1877F2
  val badgeColor = FacebookVerifiedBlue
  val contentDesc = if (isCeo) "CEO of Bhakari Permanent Blue Badge" else "Facebook-Style Blue Verified Badge"

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.then(
      if (onClick != null) {
        Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable { onClick() }
      } else {
        Modifier
      }
    )
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .background(badgeColor)
    ) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = contentDesc,
        tint = Color.White,
        modifier = Modifier.size(size * 0.68f)
      )
    }

    if (showLabel || isCeo) {
      Spacer(modifier = Modifier.width(4.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(FacebookVerifiedBlue.copy(alpha = 0.15f))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = if (isCeo) "CEO of Bhakari" else labelText,
          style = MaterialTheme.typography.labelSmall.copy(
            color = FacebookVerifiedBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        )
      }
    }
  }
}
