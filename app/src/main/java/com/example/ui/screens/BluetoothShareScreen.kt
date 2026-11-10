package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.BluetoothDevicePeer
import com.example.model.Post
import com.example.model.TransferStatus
import com.example.model.User
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun BluetoothShareScreen(
  nearbyPeers: List<BluetoothDevicePeer>,
  isScanning: Boolean,
  isDiscoverable: Boolean,
  currentLanguage: AppLanguage,
  currentUser: User?,
  latestPost: Post?,
  lastNotice: String?,
  onStartScan: () -> Unit,
  onToggleDiscoverable: () -> Unit,
  onSharePostWithPeer: (String, Post) -> Unit,
  onShareContactWithPeer: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI

  // Radar Pulse Animation
  val infiniteTransition = rememberInfiniteTransition(label = "radarPulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.35f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
      // Header Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("bluetooth_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = LanguageStrings.get("bluetooth_title", currentLanguage),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
          Text(
            text = LanguageStrings.get("bluetooth_subtitle", currentLanguage),
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 11.sp
            )
          )
        }

        IconButton(
          onClick = onStartScan,
          modifier = Modifier
            .clip(CircleShape)
            .background(VerifiedGreen.copy(alpha = 0.15f))
            .testTag("rescan_bluetooth_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Rescan",
            tint = VerifiedGreen
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
      ) {
        // Radar Visualization Card
        item {
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 14.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // Pulsing Radar Ring
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(130.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(120.dp)
                    .scale(if (isScanning) pulseScale else 1f)
                    .clip(CircleShape)
                    .background(VerifiedGreen.copy(alpha = if (isScanning) 0.15f else 0.08f))
                )
                Box(
                  modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(VerifiedGreen.copy(alpha = if (isScanning) 0.25f else 0.15f))
                )
                Box(
                  modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(VerifiedGreen),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isScanning) Icons.Default.BluetoothSearching else Icons.Default.Bluetooth,
                    contentDescription = "Bluetooth",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = if (isScanning)
                  LanguageStrings.get("bluetooth_scanning", currentLanguage)
                else
                  LanguageStrings.get("bluetooth_radar_active", currentLanguage),
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isScanning) BhakariGold else VerifiedGreen
                )
              )

              Text(
                text = if (isNepali)
                  "वरपरका मोबाइलहरूसँग सिधै इन्टरनेट बिना डेटा सेयर गर्नुहोस्"
                else
                  "Share directly device-to-device without active Wi-Fi or cellular data",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 11.sp
                )
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Discoverable Switch Row
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = LanguageStrings.get("broadcast_toggle", currentLanguage),
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )

                Switch(
                  checked = isDiscoverable,
                  onCheckedChange = { onToggleDiscoverable() },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = VerifiedGreen
                  ),
                  modifier = Modifier.testTag("discoverable_switch")
                )
              }
            }
          }
        }

        // Transfer Notice / Success Banner
        if (lastNotice != null) {
          item {
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = VerifiedGreen.copy(alpha = 0.15f)
              ),
              border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(VerifiedGreen, BhakariGold))
              ),
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = VerifiedGreen,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = lastNotice,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = VerifiedGreen
                  )
                )
              }
            }
          }
        }

        // Section Title: Nearby Users Found
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${LanguageStrings.get("devices_found", currentLanguage)} (${nearbyPeers.size})",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )

            Text(
              text = LanguageStrings.get("mesh_mode", currentLanguage),
              style = MaterialTheme.typography.labelSmall.copy(
                color = VerifiedGreen,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }

        // List of Nearby Bluetooth Peers
        items(nearbyPeers, key = { it.id }) { peer ->
          NearbyPeerCard(
            peer = peer,
            currentLanguage = currentLanguage,
            onSharePost = {
              if (latestPost != null) {
                onSharePostWithPeer(peer.id, latestPost)
              } else {
                Toast.makeText(context, "No post to share", Toast.LENGTH_SHORT).show()
              }
            },
            onShareContact = {
              onShareContactWithPeer(peer.id)
            }
          )
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
    }
  }
}

@Composable
private fun NearbyPeerCard(
  peer: BluetoothDevicePeer,
  currentLanguage: AppLanguage,
  onSharePost: () -> Unit,
  onShareContact: () -> Unit
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("peer_card_${peer.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (peer.associatedUser != null) {
          UserAvatar(
            user = peer.associatedUser,
            size = 44.dp,
            showBorder = peer.associatedUser.isCeo,
            showVerifiedBadge = true
          )
        } else {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color.Gray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bluetooth,
              contentDescription = null,
              tint = Color.Gray,
              modifier = Modifier.size(24.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = peer.name,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            )
            if (peer.associatedUser?.isCeo == true) {
              Spacer(modifier = Modifier.width(4.dp))
              VerifiedBadge(size = 14.dp)
            }
          }

          Text(
            text = "MAC: ${peer.deviceAddress} • ${peer.rssi} dBm (Very Good)",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 10.sp
            )
          )
        }

        // Status Badge
        when (peer.transferStatus) {
          TransferStatus.SENDING -> {
            CircularProgressIndicator(
              modifier = Modifier.size(22.dp),
              color = VerifiedGreen,
              strokeWidth = 2.5.dp
            )
          }
          TransferStatus.COMPLETED -> {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Completed",
              tint = VerifiedGreen,
              modifier = Modifier.size(24.dp)
            )
          }
          else -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(VerifiedGreen.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "Nearby",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = VerifiedGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Share Action Buttons for this peer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onSharePost,
          enabled = peer.transferStatus != TransferStatus.SENDING,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BhakariGold),
          modifier = Modifier
            .weight(1f)
            .testTag("send_post_to_${peer.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = LanguageStrings.get("share_post_btn", currentLanguage),
            color = Color.Black,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        OutlinedButton(
          onClick = onShareContact,
          enabled = peer.transferStatus != TransferStatus.SENDING,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("send_contact_to_${peer.id}")
        ) {
          Icon(
            imageVector = Icons.Default.ContactPage,
            contentDescription = null,
            tint = VerifiedGreen,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = LanguageStrings.get("share_contact_btn", currentLanguage),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}
