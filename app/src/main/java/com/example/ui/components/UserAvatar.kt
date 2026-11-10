package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.User
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun UserAvatar(
  user: User,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  showBorder: Boolean = false,
  showVerifiedBadge: Boolean = true
) {
  val borderBrush = if (user.isCeo) {
    Brush.sweepGradient(listOf(VerifiedGreen, BhakariGold, VerifiedGreen))
  } else if (user.isVerified) {
    Brush.linearGradient(listOf(VerifiedGreen, Color(0xFF3B82F6)))
  } else {
    Brush.linearGradient(listOf(BhakariGold, Color(0xFFE11D48)))
  }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.size(size)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(size)
        .then(
          if (showBorder) {
            Modifier
              .border(2.dp, borderBrush, CircleShape)
              .padding(2.dp)
          } else {
            Modifier
          }
        )
        .clip(CircleShape)
        .background(
          if (user.avatarRes == null) Color(user.avatarColorHex) else Color.Transparent
        )
    ) {
      if (user.avatarRes != null) {
        Image(
          painter = painterResource(id = user.avatarRes),
          contentDescription = user.name,
          modifier = Modifier.clip(CircleShape),
          contentScale = ContentScale.Crop
        )
      } else {
        val initials = user.name.split(" ")
          .take(2)
          .mapNotNull { it.firstOrNull()?.toString() }
          .joinToString("")
          .ifEmpty { "B" }
        Text(
          text = initials,
          color = Color.White,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.4f).sp
          )
        )
      }
    }

    if (showVerifiedBadge && (user.isCeo || user.isVerified)) {
      VerifiedBadge(
        size = (size * 0.35f).coerceAtLeast(14.dp),
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .offset(x = 2.dp, y = 2.dp)
          .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
      )
    }
  }
}
