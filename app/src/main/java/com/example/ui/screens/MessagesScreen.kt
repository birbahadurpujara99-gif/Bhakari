package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.ChatMessage
import com.example.model.User
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.VerifiedGreen

@Composable
fun MessagesScreen(
  currentLanguage: AppLanguage,
  currentUser: User?,
  ceoUser: User,
  chatMessages: Map<String, List<ChatMessage>>,
  onSendMessage: (String, String) -> Unit,
  onOpenCeoProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var activeChatPeer by remember { mutableStateOf<User?>(null) }
  var messageInputText by remember { mutableStateOf("") }

  // If in active chat room, handle Back to return to chat list
  if (activeChatPeer != null) {
    BackHandler { activeChatPeer = null }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    if (activeChatPeer != null) {
      // Chat Room View
      val peer = activeChatPeer!!
      val messages = chatMessages[peer.id] ?: emptyList()

      Column(
        modifier = Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .imePadding()
          .testTag("chat_room_view")
      ) {
        // Chat Room Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { activeChatPeer = null },
            modifier = Modifier.testTag("chat_room_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          Row(
            modifier = Modifier
              .weight(1f)
              .clickable {
                if (peer.isCeo) onOpenCeoProfile()
              },
            verticalAlignment = Alignment.CenterVertically
          ) {
            UserAvatar(
              user = peer,
              size = 40.dp,
              showBorder = peer.isCeo,
              showVerifiedBadge = true
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isNepali) peer.nepaliName else peer.name,
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )
                if (peer.isCeo) {
                  Spacer(modifier = Modifier.width(4.dp))
                  VerifiedBadge(size = 14.dp)
                }
              }
              Text(
                text = if (peer.isCeo)
                  (if (isNepali) "संस्थापक तथा सिईओ • सक्रिय" else "CEO & Founder • Active")
                else
                  "@${peer.username}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (peer.isCeo) VerifiedGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 11.sp
                )
              )
            }
          }
        }

        // Message Bubbles List
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 14.dp),
          contentPadding = PaddingValues(vertical = 12.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(messages) { msg ->
            MessageBubble(
              message = msg,
              isNepali = isNepali
            )
          }
        }

        // Message Input Field Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .navigationBarsPadding(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = messageInputText,
            onValueChange = { messageInputText = it },
            placeholder = {
              Text(
                text = LanguageStrings.get("type_message", currentLanguage),
                fontSize = 14.sp
              )
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
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
              .background(if (messageInputText.isNotBlank()) BhakariGold else Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
          ) {
            IconButton(
              onClick = {
                if (messageInputText.isNotBlank()) {
                  onSendMessage(peer.id, messageInputText)
                  messageInputText = ""
                }
              },
              enabled = messageInputText.isNotBlank(),
              modifier = Modifier.testTag("chat_send_button")
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
    } else {
      // Conversations List
      Column(
        modifier = Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .padding(horizontal = 14.dp, vertical = 8.dp)
          .testTag("messages_list_view")
      ) {
        Text(
          text = LanguageStrings.get("messages_title", currentLanguage),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Direct Chat Banner with CEO Bir Pujara (Prominently featured)
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = VerifiedGreen.copy(alpha = 0.12f)
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
              listOf(VerifiedGreen, BhakariGold)
            )
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { activeChatPeer = ceoUser }
            .testTag("ceo_chat_pinned_item")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            UserAvatar(
              user = ceoUser,
              size = 50.dp,
              showBorder = true,
              showVerifiedBadge = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = LanguageStrings.get("chat_with_ceo", currentLanguage),
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )
                Spacer(modifier = Modifier.width(4.dp))
                VerifiedBadge(size = 14.dp)
              }

              Text(
                text = LanguageStrings.get("ceo_available", currentLanguage),
                style = MaterialTheme.typography.bodySmall.copy(
                  color = VerifiedGreen,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              )

              val lastCeoMsg = chatMessages[ceoUser.id]?.lastOrNull()
              if (lastCeoMsg != null) {
                Text(
                  text = if (isNepali && lastCeoMsg.nepaliText.isNotBlank()) lastCeoMsg.nepaliText else lastCeoMsg.text,
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  ),
                  maxLines = 1
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Other active community chats
        val otherUsers = listOf(
          User("aarav", "Aarav Sharma", "आरभ शर्मा", "aarav_nepal", avatarColorHex = 0xFF3B82F6, isVerified = true),
          User("prakriti", "Prakriti Shrestha", "प्रकृति श्रेष्ठ", "prakriti_vibes", avatarColorHex = 0xFFEC4899)
        )

        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(otherUsers) { user ->
            Card(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { activeChatPeer = user }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                UserAvatar(user = user, size = 44.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = if (isNepali) user.nepaliName else user.name,
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    )
                    if (user.isVerified) {
                      Spacer(modifier = Modifier.width(4.dp))
                      VerifiedBadge(size = 13.dp)
                    }
                  }
                  Text(
                    text = if (isNepali) "नमस्ते! भकारीमा स्वागत छ।" else "Hey there! Welcome to Bhakari.",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MessageBubble(
  message: ChatMessage,
  isNepali: Boolean
) {
  val isMe = message.isFromMe
  val bubbleText = if (isNepali && message.nepaliText.isNotBlank()) message.nepaliText else message.text

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isMe) 16.dp else 4.dp,
            bottomEnd = if (isMe) 4.dp else 16.dp
          )
        )
        .background(
          if (isMe) BhakariGold else MaterialTheme.colorScheme.surfaceVariant
        )
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Column {
        Text(
          text = bubbleText,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = if (isMe) Color.Black else MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
          )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = message.timestamp,
          style = MaterialTheme.typography.labelSmall.copy(
            color = if (isMe) Color.Black.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontSize = 10.sp
          ),
          modifier = Modifier.align(Alignment.End)
        )
      }
    }
  }
}
