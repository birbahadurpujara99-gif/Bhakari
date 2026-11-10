package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.Post
import com.example.model.User
import com.example.ui.components.PostCard
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariNavy
import com.example.ui.theme.VerifiedGreen

@Composable
fun CeoProfileScreen(
  ceoUser: User,
  ceoPosts: List<Post>,
  currentLanguage: AppLanguage,
  onBack: () -> Unit,
  onMessageCeo: () -> Unit,
  onBluetoothShareWithCeo: () -> Unit,
  onLikePost: (String) -> Unit,
  onOpenComments: (Post) -> Unit,
  onOpenShare: (Post) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var isFollowing by remember { mutableStateOf(ceoUser.isFollowedByMe) }
  var followersCount by remember { mutableStateOf(ceoUser.followersCount) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("ceo_profile_screen"),
      contentPadding = PaddingValues(bottom = 80.dp)
    ) {
      // Top Cover Banner with Himalayan view & Navigation
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.bhakari_cover_art),
            contentDescription = "Bhakari Cover Art",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Gradient overlay for readability
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                )
              )
          )

          // Top App Bar inside cover
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .statusBarsPadding()
              .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .testTag("ceo_back_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(VerifiedGreen)
                .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isNepali) "आधिकारिक सिईओ प्रोफाइल" else "Official CEO Profile",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                )
              }
            }
          }
        }
      }

      // CEO Header Info Section
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          // Avatar floating over banner
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .offset(y = (-44).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(3.5.dp, VerifiedGreen, CircleShape)
                .padding(3.dp)
            ) {
              Image(
                painter = painterResource(id = R.drawable.ceo_bir_pujara),
                contentDescription = "CEO Bir Pujara",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(90.dp)
                  .clip(CircleShape)
              )
            }

            // Follow & Message Quick Actions
            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.padding(bottom = 4.dp)
            ) {
              Button(
                onClick = {
                  isFollowing = !isFollowing
                  followersCount += if (isFollowing) 1 else -1
                  Toast.makeText(
                    context,
                    if (isFollowing) (if (isNepali) "सिईओलाई फलो गरियो" else "Following CEO Bir Pujara")
                    else (if (isNepali) "अनफलो गरियो" else "Unfollowed"),
                    Toast.LENGTH_SHORT
                  ).show()
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (isFollowing) MaterialTheme.colorScheme.surfaceVariant else VerifiedGreen
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("ceo_follow_button")
              ) {
                Text(
                  text = if (isFollowing)
                    LanguageStrings.get("following_active", currentLanguage)
                  else
                    LanguageStrings.get("follow", currentLanguage),
                  color = if (isFollowing) MaterialTheme.colorScheme.onSurface else Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }

              Button(
                onClick = onMessageCeo,
                colors = ButtonDefaults.buttonColors(containerColor = BhakariGold),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("ceo_message_button")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Chat,
                  contentDescription = null,
                  tint = Color.Black,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = LanguageStrings.get("message_ceo", currentLanguage),
                  color = Color.Black,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }
          }

          // CEO Name & Green Verified Tick
          Column(modifier = Modifier.offset(y = (-36).dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = if (isNepali) ceoUser.nepaliName else ceoUser.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )

              // Prominent Green Verified Tick
              VerifiedBadge(size = 22.dp, showLabel = true, labelText = "CEO • Verified")
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = if (isNepali) ceoUser.nepaliTitle else ceoUser.title,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = BhakariGold
              )
            )

            Text(
              text = "@${ceoUser.username} • Kathmandu, Nepal",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bio
            Text(
              text = if (isNepali) ceoUser.nepaliBio else ceoUser.bio,
              style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Card (Followers, Following, Posts)
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                ProfileStatColumn(
                  value = "48",
                  label = LanguageStrings.get("posts", currentLanguage)
                )
                ProfileStatColumn(
                  value = "${followersCount / 1000}K",
                  label = LanguageStrings.get("followers", currentLanguage)
                )
                ProfileStatColumn(
                  value = "${ceoUser.followingCount}",
                  label = LanguageStrings.get("following", currentLanguage)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Official Verification Certificate Explainer Card
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = VerifiedGreen.copy(alpha = 0.1f)
              ),
              border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(VerifiedGreen, Color(0xFF047857)))
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                VerifiedBadge(size = 24.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = if (isNepali) "हरियो प्रमाणित टिक (Official Green Badge)" else "Verified Account (Green Tick)",
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = VerifiedGreen
                    )
                  )
                  Text(
                    text = if (isNepali)
                      "यो खाता भकारीका संस्थापक तथा सिईओ वीर पुजाराको आधिकारिक खाता हो भनी प्रमाणित गरिएको छ।"
                    else
                      "Identity verified: Official account of Bir Pujara, Founder & CEO of Bhakari Social Media.",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      fontSize = 11.sp
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact & Bluetooth Sharing Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = onBluetoothShareWithCeo,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("ceo_bluetooth_connect_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Bluetooth,
                  contentDescription = null,
                  tint = VerifiedGreen,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isNepali) "ब्लुटुथबाट जोड्नुहोस्" else "Bluetooth Connect",
                  color = MaterialTheme.colorScheme.onSurface,
                  fontSize = 12.sp
                )
              }

              OutlinedButton(
                onClick = {
                  Toast.makeText(
                    context,
                    "CEO Email: ${ceoUser.email}",
                    Toast.LENGTH_LONG
                  ).show()
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("ceo_email_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Email,
                  contentDescription = null,
                  tint = BhakariGold,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isNepali) "इमेल सम्पर्क" else "Email Contact",
                  color = MaterialTheme.colorScheme.onSurface,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Heading for CEO Posts
            Text(
              text = LanguageStrings.get("ceo_posts_heading", currentLanguage),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
          }
        }
      }

      // CEO's Post Archive
      items(ceoPosts, key = { it.id }) { post ->
        PostCard(
          post = post,
          currentLanguage = currentLanguage,
          onLikeClick = { onLikePost(post.id) },
          onCommentClick = { onOpenComments(post) },
          onShareClick = { onOpenShare(post) },
          onBluetoothShareClick = { onBluetoothShareWithCeo() },
          onAuthorClick = {}
        )
      }
    }
  }
}

@Composable
private fun ProfileStatColumn(value: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp
      )
    )
  }
}
