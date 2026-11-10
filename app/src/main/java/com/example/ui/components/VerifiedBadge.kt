package com.example.ui.components

import androidx.compose.foundation.background
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
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenLight

@Composable
fun VerifiedBadge(
  modifier: Modifier = Modifier,
  size: Dp = 16.dp,
  showLabel: Boolean = false,
  labelText: String = "CEO • Verified"
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .size(size)
        .clip(CircleShape)
        .background(VerifiedGreen)
    ) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "Verified Green Checkmark",
        tint = Color.White,
        modifier = Modifier.size(size * 0.7f)
      )
    }

    if (showLabel) {
      Spacer(modifier = Modifier.width(4.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(VerifiedGreen.copy(alpha = 0.15f))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = labelText,
          style = MaterialTheme.typography.labelSmall.copy(
            color = VerifiedGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        )
      }
    }
  }
}
