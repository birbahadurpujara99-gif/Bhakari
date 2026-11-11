package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.Post
import com.example.model.User
import com.example.ui.components.PostCard
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariNavy
import com.example.ui.theme.FacebookVerifiedBlue
import com.example.ui.theme.VerifiedGreen

@Composable
fun FeedScreen(
  posts: List<Post>,
  currentUser: User?,
  ceoUser: User,
  allUsers: List<User> = emptyList(),
  currentLanguage: AppLanguage,
  onLikePost: (String) -> Unit,
  onOpenComments: (Post) -> Unit,
  onOpenShare: (Post) -> Unit,
  onBluetoothShare: (Post) -> Unit,
  onOpenCeoProfile: () -> Unit,
  onNavigateToCreatePost: () -> Unit,
  onOpenInstallDialog: () -> Unit,
  onBadgeClick: ((User) -> Unit)? = null,
  onReportClick: ((Post) -> Unit)? = null,
  onOpenUserProfile: ((User) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var selectedFilter by remember { mutableIntStateOf(0) } // 0: All, 1: CEO, 2: Trending, 3: Culture
  var showPwaBanner by remember { mutableStateOf(true) }
  var searchQuery by remember { mutableStateOf("") }

  val matchingUsers = if (searchQuery.isNotBlank()) {
    allUsers.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
      it.nepaliName.contains(searchQuery) ||
      it.username.contains(searchQuery, ignoreCase = true) ||
      it.title.contains(searchQuery, ignoreCase = true)
    }
  } else emptyList()

  val filteredPosts = posts.filter { post ->
    val matchesSearch = searchQuery.isBlank() ||
      post.content.contains(searchQuery, ignoreCase = true) ||
      post.nepaliContent.contains(searchQuery) ||
      post.author.name.contains(searchQuery, ignoreCase = true) ||
      post.author.nepaliName.contains(searchQuery) ||
      post.tags.any { it.contains(searchQuery, ignoreCase = true) }

    val matchesFilter = when (selectedFilter) {
      1 -> post.author.isCeo || post.isCeoAnnouncement
      2 -> true
      3 -> post.tags.any { tag -> tag.contains("Culture") || tag.contains("Festival") }
      else -> true
    }

    matchesSearch && matchesFilter
  }.let { list ->
    if (selectedFilter == 2) list.sortedByDescending { it.likesCount } else list
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("feed_lazy_column"),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Search Bar
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 14.dp, vertical = 8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = if (isNepali) "भाकारी खोज्नुहोस् (प्रयोगकर्ता, पोस्ट, विषय)..." else "Search Bhakari (users, posts, tags)...",
              fontSize = 13.sp
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear search",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(24.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("feed_search_input")
        )
      }
    }

    // Matching Users Row when searching
    if (searchQuery.isNotBlank() && matchingUsers.isNotEmpty()) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(vertical = 8.dp)
        ) {
          Text(
            text = if (isNepali) "भेटिएका प्रयोगकर्ताहरू" else "Matching Users",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
          )

          LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(matchingUsers) { user ->
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                  .clickable {
                    if (user.isCeo) onOpenCeoProfile() else onOpenUserProfile?.invoke(user)
                  }
                  .padding(vertical = 4.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  UserAvatar(user = user, size = 32.dp, onBadgeClick = { onBadgeClick?.invoke(user) })
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = user.name,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                      )
                      if (user.hasBlueBadge) {
                        Spacer(modifier = Modifier.width(4.dp))
                        VerifiedBadge(
                          size = 13.dp,
                          isCeo = user.isCeo,
                          onClick = { onBadgeClick?.invoke(user) }
                        )
                      }
                    }
                    Text(
                      text = "@${user.username}",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                      )
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
    // Top Stories / Moments Bar
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(vertical = 10.dp)
      ) {
        Text(
          text = LanguageStrings.get("stories_title", currentLanguage),
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          ),
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )

        LazyRow(
          contentPadding = PaddingValues(horizontal = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.padding(top = 6.dp)
        ) {
          // CEO Bir Pujara Story (Prominent with green glowing verified badge)
          item {
            StoryItem(
              user = ceoUser,
              label = if (isNepali) "सिईओ वीर" else "CEO Bir",
              hasStory = true,
              isCeo = true,
              onClick = onOpenCeoProfile,
              testTag = "ceo_story_item"
            )
          }

          // Current User "Add Story"
          if (currentUser != null && !currentUser.isCeo) {
            item {
              StoryItem(
                user = currentUser,
                label = if (isNepali) "तपाईंको कथा" else "Your Story",
                hasStory = false,
                isAddStory = true,
                onClick = onNavigateToCreatePost,
                testTag = "user_add_story_item"
              )
            }
          }

          // Other Creators
          item {
            StoryItem(
              user = User("aarav", "Aarav Sharma", "आरभ शर्मा", "aarav", avatarColorHex = 0xFF3B82F6),
              label = "Aarav",
              hasStory = true,
              onClick = {},
              testTag = "story_aarav"
            )
          }
          item {
            StoryItem(
              user = User("prakriti", "Prakriti Shrestha", "प्रकृति श्रेष्ठ", "prakriti", avatarColorHex = 0xFFEC4899),
              label = "Prakriti",
              hasStory = true,
              onClick = {},
              testTag = "story_prakriti"
            )
          }
          item {
            StoryItem(
              user = User("himal", "Himal Vibes", "हिमाल भाइब्स", "himal", avatarColorHex = 0xFF10B981),
              label = "Himal",
              hasStory = true,
              onClick = {},
              testTag = "story_himal"
            )
          }
        }
      }
    }

    // PWA Chrome Install Banner
    item {
      AnimatedVisibility(visible = showPwaBanner) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = VerifiedGreen.copy(alpha = 0.12f)
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(VerifiedGreen, BhakariGold))
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("pwa_install_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(VerifiedGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = LanguageStrings.get("pwa_banner_title", currentLanguage),
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              Text(
                text = LanguageStrings.get("pwa_banner_subtitle", currentLanguage),
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 11.sp
                )
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(VerifiedGreen)
                .clickable { onOpenInstallDialog() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("pwa_banner_install_button")
            ) {
              Text(
                text = LanguageStrings.get("install_app_btn", currentLanguage),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
            }

            IconButton(
              onClick = { showPwaBanner = false },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }

    // "What's on your mind? / मनमा के छ?" Post Composer Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onNavigateToCreatePost() },
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (currentUser != null) {
              UserAvatar(user = currentUser, size = 40.dp)
            } else {
              UserAvatar(user = ceoUser, size = 40.dp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Text(
                text = LanguageStrings.get("whats_on_mind", currentLanguage),
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            ComposerActionChip(
              icon = Icons.Default.Image,
              tint = BhakariGold,
              label = LanguageStrings.get("btn_photo", currentLanguage),
              onClick = onNavigateToCreatePost
            )
            ComposerActionChip(
              icon = Icons.Default.Videocam,
              tint = BhakariCrimson,
              label = LanguageStrings.get("btn_video", currentLanguage),
              onClick = onNavigateToCreatePost
            )
            ComposerActionChip(
              icon = Icons.Default.Edit,
              tint = VerifiedGreen,
              label = LanguageStrings.get("btn_text", currentLanguage),
              onClick = onNavigateToCreatePost
            )
          }
        }
      }
    }

    // Filter Chips Row
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val filterTitles = listOf(
          LanguageStrings.get("filter_all", currentLanguage),
          LanguageStrings.get("filter_ceo", currentLanguage),
          LanguageStrings.get("filter_trending", currentLanguage),
          LanguageStrings.get("filter_nepal", currentLanguage)
        )

        items(filterTitles.indices.toList()) { index ->
          FilterChip(
            selected = selectedFilter == index,
            onClick = { selectedFilter = index },
            label = {
              Text(
                text = filterTitles[index],
                fontWeight = if (selectedFilter == index) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = if (index == 1) VerifiedGreen.copy(alpha = 0.2f) else BhakariGold.copy(alpha = 0.2f),
              selectedLabelColor = if (index == 1) VerifiedGreen else BhakariGold
            ),
            shape = RoundedCornerShape(18.dp)
          )
        }
      }
    }

    // Feed Posts List
    items(filteredPosts, key = { it.id }) { post ->
      PostCard(
        post = post,
        currentLanguage = currentLanguage,
        onLikeClick = { onLikePost(post.id) },
        onCommentClick = { onOpenComments(post) },
        onShareClick = { onOpenShare(post) },
        onBluetoothShareClick = { onBluetoothShare(post) },
        onBadgeClick = onBadgeClick,
        onReportClick = onReportClick,
        onAuthorClick = {
          if (post.author.isCeo) {
            onOpenCeoProfile()
          } else {
            onOpenUserProfile?.invoke(post.author)
          }
        }
      )
    }
  }
}

@Composable
private fun StoryItem(
  user: User,
  label: String,
  hasStory: Boolean,
  isCeo: Boolean = false,
  isAddStory: Boolean = false,
  onClick: () -> Unit,
  testTag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable { onClick() }
      .testTag(testTag)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(62.dp)
    ) {
      val borderBrush = when {
        isCeo -> Brush.sweepGradient(listOf(VerifiedGreen, BhakariGold, VerifiedGreen))
        hasStory -> Brush.linearGradient(listOf(BhakariGold, BhakariCrimson))
        else -> Brush.linearGradient(listOf(Color.Gray.copy(alpha = 0.4f), Color.Gray.copy(alpha = 0.4f)))
      }

      Box(
        modifier = Modifier
          .size(60.dp)
          .border(2.5.dp, borderBrush, CircleShape)
          .padding(3.dp),
        contentAlignment = Alignment.Center
      ) {
        UserAvatar(
          user = user,
          size = 52.dp,
          showVerifiedBadge = false
        )
      }

      if (isAddStory) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .align(Alignment.BottomEnd)
            .clip(CircleShape)
            .background(BhakariGold),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(14.dp)
          )
        }
      } else if (isCeo) {
        VerifiedBadge(
          size = 18.dp,
          isCeo = true,
          modifier = Modifier.align(Alignment.BottomEnd)
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        fontWeight = if (isCeo) FontWeight.Bold else FontWeight.Medium,
        color = if (isCeo) VerifiedGreen else MaterialTheme.colorScheme.onSurface
      ),
      maxLines = 1
    )
  }
}

@Composable
private fun ComposerActionChip(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tint: Color,
  label: String,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = tint,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      )
    )
  }
}
