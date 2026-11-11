package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@Composable
fun EditProfileDialog(
  user: User,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onSaveProfile: (updatedUser: User) -> Unit,
  onDeleteAccount: () -> Unit
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val isHindi = currentLanguage == AppLanguage.HINDI

  var name by remember { mutableStateOf(user.name) }
  var nepaliName by remember { mutableStateOf(user.nepaliName) }
  var username by remember { mutableStateOf(user.username) }
  var title by remember { mutableStateOf(user.title) }
  var bio by remember { mutableStateOf(user.bio) }
  var nepaliBio by remember { mutableStateOf(user.nepaliBio) }
  var phone by remember { mutableStateOf(user.phone) }
  var email by remember { mutableStateOf(user.email) }
  
  var avatarUrl by remember { mutableStateOf(user.avatarUrl) }
  var coverUrl by remember { mutableStateOf(user.coverUrl) }
  
  var isPrivate by remember { mutableStateOf(user.isPrivateAccount) }
  var allowMessages by remember { mutableStateOf(user.allowMessagesFromAnyone) }
  var allowBluetooth by remember { mutableStateOf(user.allowBluetoothDiscovery) }
  
  var showDeleteConfirmDialog by remember { mutableStateOf(false) }

  // Zero-permission Android Photo Picker for Profile Picture
  val avatarPhotoPicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      avatarUrl = uri.toString()
      Toast.makeText(context, "Profile picture selected!", Toast.LENGTH_SHORT).show()
    }
  }

  // Zero-permission Android Photo Picker for Cover Photo
  val coverPhotoPicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      coverUrl = uri.toString()
      Toast.makeText(context, "Cover photo selected!", Toast.LENGTH_SHORT).show()
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .testTag("edit_profile_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = LanguageStrings.get("edit_profile", currentLanguage),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_edit_profile_btn")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cover Photo & Avatar Customization Section
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
        ) {
          // Cover Banner
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(100.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(FacebookVerifiedBlue.copy(alpha = 0.2f))
              .clickable {
                coverPhotoPicker.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
              .testTag("change_cover_button"),
            contentAlignment = Alignment.Center
          ) {
            if (coverUrl.isNotBlank()) {
              AsyncImage(
                model = coverUrl,
                contentDescription = "Cover photo",
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
            // Edit Cover Overlay Tag
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = LanguageStrings.get("change_cover", currentLanguage),
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          // Avatar (overlapping bottom-start)
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(start = 16.dp)
              .size(72.dp)
          ) {
            UserAvatar(
              user = user.copy(avatarUrl = avatarUrl),
              size = 72.dp,
              showBorder = true,
              showVerifiedBadge = false
            )
            // Camera icon badge to change photo
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(26.dp)
                .clip(CircleShape)
                .background(FacebookVerifiedBlue)
                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                .clickable {
                  avatarPhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                }
                .testTag("change_avatar_button"),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.CameraAlt, contentDescription = "Change avatar", tint = Color.White, modifier = Modifier.size(14.dp))
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Personal Information Inputs
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text(LanguageStrings.get("fullname_label", currentLanguage)) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_name_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = nepaliName,
          onValueChange = { nepaliName = it },
          label = { Text("नेपालीमा नाम (Nepali Name)") },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = username,
          onValueChange = { username = it },
          label = { Text(LanguageStrings.get("username_label", currentLanguage)) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          prefix = { Text("@") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_username_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(LanguageStrings.get("title_label", currentLanguage)) },
          placeholder = { Text("e.g. Cultural Creator, Tech Engineer, Traveler") },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = bio,
          onValueChange = { bio = it },
          label = { Text(LanguageStrings.get("bio_label", currentLanguage)) },
          shape = RoundedCornerShape(12.dp),
          minLines = 3,
          maxLines = 4,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("edit_bio_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text(LanguageStrings.get("phone_label", currentLanguage)) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Account Settings Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = LanguageStrings.get("privacy_settings", currentLanguage),
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Private Account Switch
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = LanguageStrings.get("private_account", currentLanguage),
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                  text = if (isNepali) "केवल स्वीकृत साथीहरूले पोस्ट हेर्न पाउनेछन्" else "Only approved followers can see your posts",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
              Switch(
                checked = isPrivate,
                onCheckedChange = { isPrivate = it },
                colors = SwitchDefaults.colors(checkedThumbColor = FacebookVerifiedBlue)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Direct Messages Switch
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = LanguageStrings.get("allow_messages", currentLanguage),
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                  text = if (isNepali) "सबै भकारी प्रयोगकर्ताहरूले च्याट गर्न पाउनेछन्" else "Anyone can send you direct messages",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
              Switch(
                checked = allowMessages,
                onCheckedChange = { allowMessages = it },
                colors = SwitchDefaults.colors(checkedThumbColor = FacebookVerifiedBlue)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bluetooth Discoverability Switch
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = LanguageStrings.get("bluetooth_discoverable", currentLanguage),
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                  text = if (isNepali) "वरपरका साथीहरूको राडारमा तपाईंको डिभाइस देखिनेछ" else "Visible to nearby peers in offline radar",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
              Switch(
                checked = allowBluetooth,
                onCheckedChange = { allowBluetooth = it },
                colors = SwitchDefaults.colors(checkedThumbColor = FacebookVerifiedBlue)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete Account Danger Option
        OutlinedButton(
          onClick = { showDeleteConfirmDialog = true },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BhakariCrimson),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("delete_account_btn")
        ) {
          Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = LanguageStrings.get("delete_account", currentLanguage), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: Cancel & Save Changes
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(LanguageStrings.get("cancel", currentLanguage))
          }

          Button(
            onClick = {
              val updated = user.copy(
                name = name.ifBlank { user.name },
                nepaliName = nepaliName.ifBlank { user.nepaliName },
                username = username.ifBlank { user.username },
                title = title,
                bio = bio,
                phone = phone,
                email = email,
                avatarUrl = avatarUrl,
                coverUrl = coverUrl,
                isPrivateAccount = isPrivate,
                allowMessagesFromAnyone = allowMessages,
                allowBluetoothDiscovery = allowBluetooth
              )
              onSaveProfile(updated)
              Toast.makeText(context, LanguageStrings.get("profile_saved_toast", currentLanguage), Toast.LENGTH_SHORT).show()
              onDismiss()
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
            modifier = Modifier
              .weight(1.5f)
              .testTag("save_profile_button")
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(LanguageStrings.get("save_changes", currentLanguage), fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }

  // Delete Account Confirmation Alert Dialog
  if (showDeleteConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirmDialog = false },
      title = { Text(LanguageStrings.get("delete_account", currentLanguage), fontWeight = FontWeight.Bold) },
      text = { Text(LanguageStrings.get("delete_account_confirm", currentLanguage)) },
      confirmButton = {
        Button(
          onClick = {
            showDeleteConfirmDialog = false
            onDeleteAccount()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BhakariCrimson)
        ) {
          Text(LanguageStrings.get("delete_account", currentLanguage))
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirmDialog = false }) {
          Text(LanguageStrings.get("cancel", currentLanguage))
        }
      }
    )
  }
}
