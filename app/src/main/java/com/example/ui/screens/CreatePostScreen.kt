package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.MediaType
import com.example.model.User
import com.example.ui.components.UserAvatar
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun CreatePostScreen(
  currentUser: User?,
  currentLanguage: AppLanguage,
  onPublish: (String, MediaType, String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI

  var postContent by remember { mutableStateOf("") }
  var selectedMediaType by remember { mutableStateOf(MediaType.PHOTO) }
  var selectedTag by remember { mutableStateOf("#BhakariCommunity") }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .imePadding()
        .verticalScroll(rememberScrollState())
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = LanguageStrings.get("create_post_title", currentLanguage),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }

        Button(
          onClick = {
            if (postContent.isNotBlank()) {
              onPublish(postContent, selectedMediaType, selectedTag)
              Toast.makeText(
                context,
                LanguageStrings.get("post_published", currentLanguage),
                Toast.LENGTH_SHORT
              ).show()
              onBack()
            } else {
              Toast.makeText(
                context,
                if (isNepali) "कृपया केही लेख्नुहोस्" else "Please write something",
                Toast.LENGTH_SHORT
              ).show()
            }
          },
          enabled = postContent.isNotBlank(),
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BhakariGold),
          modifier = Modifier.testTag("publish_post_button")
        ) {
          Text(
            text = LanguageStrings.get("publish_post", currentLanguage),
            fontWeight = FontWeight.Bold,
            color = Color.Black
          )
        }
      }

      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Author Preview
        if (currentUser != null) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
          ) {
            UserAvatar(user = currentUser, size = 42.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isNepali) currentUser.nepaliName else currentUser.name,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold
                )
              )
              Text(
                text = if (isNepali) "सार्वजनिक पोस्ट • नेपाल" else "Public Post • Nepal",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Text Input
        OutlinedTextField(
          value = postContent,
          onValueChange = { postContent = it },
          placeholder = {
            Text(
              text = LanguageStrings.get("caption_placeholder", currentLanguage),
              fontSize = 15.sp
            )
          },
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BhakariGold,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .testTag("create_post_text_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Media Type Selector
        Text(
          text = LanguageStrings.get("select_media_type", currentLanguage),
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          MediaTypeTab(
            title = LanguageStrings.get("btn_photo", currentLanguage),
            icon = Icons.Default.Image,
            isSelected = selectedMediaType == MediaType.PHOTO,
            onClick = { selectedMediaType = MediaType.PHOTO },
            modifier = Modifier.weight(1f)
          )
          MediaTypeTab(
            title = LanguageStrings.get("btn_video", currentLanguage),
            icon = Icons.Default.Videocam,
            isSelected = selectedMediaType == MediaType.VIDEO,
            onClick = { selectedMediaType = MediaType.VIDEO },
            modifier = Modifier.weight(1f)
          )
          MediaTypeTab(
            title = LanguageStrings.get("btn_text", currentLanguage),
            icon = Icons.Default.Edit,
            isSelected = selectedMediaType == MediaType.TEXT_ONLY,
            onClick = { selectedMediaType = MediaType.TEXT_ONLY },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Media Preview Card
        if (selectedMediaType == MediaType.PHOTO) {
          Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
            ) {
              Image(
                painter = painterResource(id = R.drawable.bhakari_cover_art),
                contentDescription = "Selected Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
              Box(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(8.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.Black.copy(alpha = 0.6f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "Photo Ready 📷",
                  color = Color.White,
                  fontSize = 11.sp
                )
              }
            }
          }
        } else if (selectedMediaType == MediaType.VIDEO) {
          Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
            ) {
              Image(
                painter = painterResource(id = R.drawable.bhakari_cover_art),
                contentDescription = "Selected Video",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7f
              )
              Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                tint = BhakariGold,
                modifier = Modifier.size(50.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Community Tags
        Text(
          text = if (isNepali) "समुदाय ट्याग छान्नुहोस्" else "Choose Community Tag",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val tags = listOf("#BhakariCommunity", "#NepalCulture", "#DigitalNepal", "#KathmanduVibes")
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          tags.forEach { tag ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (selectedTag == tag) BhakariGold else MaterialTheme.colorScheme.surfaceVariant)
                .clickable { selectedTag = tag }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (selectedTag == tag) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
private fun MediaTypeTab(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .background(if (isSelected) BhakariGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
      .border(
        width = if (isSelected) 1.5.dp else 0.dp,
        color = if (isSelected) BhakariGold else Color.Transparent,
        shape = RoundedCornerShape(14.dp)
      )
      .clickable { onClick() }
      .padding(vertical = 10.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) BhakariGold else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
          color = if (isSelected) BhakariGold else MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}
