package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.MediaType
import com.example.model.Post
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariNavyCard
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostCard(
  post: Post,
  currentLanguage: AppLanguage,
  onLikeClick: () -> Unit,
  onCommentClick: () -> Unit,
  onShareClick: () -> Unit,
  onBluetoothShareClick: () -> Unit,
  onAuthorClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var isPlayingVideo by remember { mutableStateOf(false) }

  val scaleAnim by animateFloatAsState(
    targetValue = if (post.isLiked) 1.15f else 1.0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "likeScale"
  )

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag("post_card_${post.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // CEO Official Announcement Tag if applicable
      if (post.isCeoAnnouncement) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
              Brush.horizontalGradient(
                listOf(BhakariGold.copy(alpha = 0.2f), VerifiedGreen.copy(alpha = 0.2f))
              )
            )
            .border(1.dp, BhakariGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            VerifiedBadge(size = 14.dp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = LanguageStrings.get("official_announcement", currentLanguage),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BhakariGold,
                fontSize = 11.sp
              )
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      // Header: Author Avatar, Name, Verified Tick, Role & Timestamp
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onAuthorClick() },
        verticalAlignment = Alignment.CenterVertically
      ) {
        UserAvatar(
          user = post.author,
          size = 46.dp,
          showBorder = post.author.isCeo,
          showVerifiedBadge = true
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (isNepali) post.author.nepaliName else post.author.name,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )

            if (post.author.isCeo) {
              Spacer(modifier = Modifier.width(6.dp))
              VerifiedBadge(size = 15.dp, showLabel = true, labelText = "CEO")
            } else if (post.author.isVerified) {
              Spacer(modifier = Modifier.width(6.dp))
              VerifiedBadge(size = 14.dp)
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "@${post.author.username}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
            Text(
              text = " • ${post.timestamp} • ${post.location}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
              )
            )
          }
        }

        // Quick Bluetooth Share Icon on top right of the card
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(VerifiedGreen.copy(alpha = 0.12f))
            .clickable { onBluetoothShareClick() }
            .padding(8.dp)
            .testTag("bluetooth_share_icon_${post.id}"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Bluetooth,
            contentDescription = "Share via Bluetooth",
            tint = VerifiedGreen,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Post Content Body
      val postText = if (isNepali && post.nepaliContent.isNotBlank()) {
        post.nepaliContent
      } else {
        post.content
      }

      Text(
        text = postText,
        style = MaterialTheme.typography.bodyLarge.copy(
          lineHeight = 22.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      )

      // Tags
      if (post.tags.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          post.tags.forEach { tag ->
            Text(
              text = tag,
              style = MaterialTheme.typography.labelSmall.copy(
                color = if (tag.contains("CEO")) BhakariGold else BhakariCrimson,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }

      // Media Section (Photo or Video)
      if (post.mediaType == MediaType.PHOTO && post.imageRes != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
        ) {
          Image(
            painter = painterResource(id = post.imageRes),
            contentDescription = "Post image",
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
          )
        }
      } else if (post.mediaType == MediaType.VIDEO && post.videoThumbnailRes != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .aspectRatio(16f / 9f)
            .background(Color.Black)
            .clickable { isPlayingVideo = !isPlayingVideo }
            .testTag("video_player_${post.id}")
        ) {
          Image(
            painter = painterResource(id = post.videoThumbnailRes),
            contentDescription = "Video thumbnail",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alpha = if (isPlayingVideo) 0.85f else 0.7f
          )

          // Video Play Overlay
          if (!isPlayingVideo) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.65f))
                .border(2.dp, BhakariGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Video",
                tint = BhakariGold,
                modifier = Modifier.size(34.dp)
              )
            }
          } else {
            // Simulated Active Video Controls
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = post.videoTitle ?: "Bhakari Video",
                  style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                )
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Audio on",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(3.dp)
                  .background(Color.Gray.copy(alpha = 0.5f))
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(3.dp)
                    .background(BhakariCrimson)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom Interaction Row: Like, Comment, Share, Bluetooth
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Like Button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onLikeClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag("like_button_${post.id}")
        ) {
          Icon(
            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            tint = if (post.isLiked) BhakariCrimson else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .size(22.dp)
              .scale(scaleAnim)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${post.likesCount}",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = if (post.isLiked) FontWeight.Bold else FontWeight.Medium,
              color = if (post.isLiked) BhakariCrimson else MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }

        // Comment Button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onCommentClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag("comment_button_${post.id}")
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Comment",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${post.commentsCount}",
            style = MaterialTheme.typography.labelMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }

        // General Share Button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onShareClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .testTag("share_button_${post.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${post.sharesCount}",
            style = MaterialTheme.typography.labelMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }

        // Bluetooth Direct Share Pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VerifiedGreen.copy(alpha = 0.12f))
            .clickable { onBluetoothShareClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("card_bluetooth_pill_${post.id}")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Bluetooth,
              contentDescription = null,
              tint = VerifiedGreen,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isNepali) "ब्लुटुथ सेयर" else "BT Share",
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
  }
}
