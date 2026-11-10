package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.Comment
import com.example.model.Post
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
  post: Post,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onAddComment: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var newCommentText by remember { mutableStateOf("") }
  val isNepali = currentLanguage == AppLanguage.NEPALI

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = Modifier.testTag("comments_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .navigationBarsPadding()
        .imePadding()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${LanguageStrings.get("comments", currentLanguage)} (${post.comments.size})",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Comments List
      if (post.comments.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (isNepali) "पहिलो प्रतिक्रिया दिनुहोस्! 💬" else "Be the first to comment! 💬",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(post.comments) { comment ->
            CommentItem(comment = comment, isNepali = isNepali)
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Comment Input Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = newCommentText,
          onValueChange = { newCommentText = it },
          placeholder = {
            Text(
              text = LanguageStrings.get("write_comment", currentLanguage),
              fontSize = 14.sp
            )
          },
          modifier = Modifier
            .weight(1f)
            .testTag("comment_input_field"),
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BhakariGold,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
          ),
          maxLines = 3
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (newCommentText.isNotBlank()) BhakariGold else Color.Gray.copy(alpha = 0.3f)),
          contentAlignment = Alignment.Center
        ) {
          IconButton(
            onClick = {
              if (newCommentText.isNotBlank()) {
                onAddComment(newCommentText)
                newCommentText = ""
              }
            },
            enabled = newCommentText.isNotBlank(),
            modifier = Modifier.testTag("submit_comment_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CommentItem(
  comment: Comment,
  isNepali: Boolean
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top
  ) {
    UserAvatar(
      user = comment.author,
      size = 36.dp,
      showVerifiedBadge = comment.author.isCeo || comment.author.isVerified
    )

    Spacer(modifier = Modifier.width(10.dp))

    Column(
      modifier = Modifier
        .weight(1f)
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        .padding(10.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = if (isNepali) comment.author.nepaliName else comment.author.name,
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
        if (comment.author.isCeo) {
          Spacer(modifier = Modifier.width(4.dp))
          VerifiedBadge(size = 12.dp)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = comment.timestamp,
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            fontSize = 10.sp
          )
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = if (isNepali && comment.nepaliText.isNotBlank()) comment.nepaliText else comment.text,
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface,
          fontSize = 13.sp
        )
      )
    }
  }
}
