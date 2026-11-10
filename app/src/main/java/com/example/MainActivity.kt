package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.BhakariRepository
import com.example.data.LanguageStrings
import com.example.model.Post
import com.example.ui.components.BhakariTopBar
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.InstallPwaDialog
import com.example.ui.components.ShareDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BluetoothShareScreen
import com.example.ui.screens.CeoProfileScreen
import com.example.ui.screens.CreatePostScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariTheme
import com.example.ui.theme.VerifiedGreen

enum class ScreenTab {
  FEED,
  MESSAGES,
  CREATE_POST,
  BLUETOOTH,
  PROFILE,
  CEO_PROFILE
}

class MainActivity : ComponentActivity() {
  private val repository = BhakariRepository()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      BhakariTheme {
        BhakariApp(repository = repository)
      }
    }
  }
}

@Composable
fun BhakariApp(repository: BhakariRepository) {
  val context = LocalContext.current
  val currentUser by repository.currentUser.collectAsState()
  val currentLanguage by repository.currentLanguage.collectAsState()
  val posts by repository.posts.collectAsState()
  val chatMessages by repository.chatMessages.collectAsState()
  val nearbyPeers by repository.nearbyPeers.collectAsState()
  val isScanning by repository.isBluetoothScanning.collectAsState()
  val isDiscoverable by repository.isDiscoverable.collectAsState()
  val lastTransferNotice by repository.lastTransferNotice.collectAsState()

  var currentTab by remember { mutableStateOf(ScreenTab.FEED) }
  var showInstallDialog by remember { mutableStateOf(false) }
  var postForComments by remember { mutableStateOf<Post?>(null) }
  var postForShare by remember { mutableStateOf<Post?>(null) }

  // If user is not logged in, show AuthScreen (Phone, Email, Signup, CEO shortcut)
  if (currentUser == null) {
    AuthScreen(
      currentLanguage = currentLanguage,
      onToggleLanguage = { repository.toggleLanguage() },
      onLoginPhone = { phone, otp ->
        repository.loginWithPhone(phone, otp)
        Toast.makeText(context, "Logged in via Phone!", Toast.LENGTH_SHORT).show()
      },
      onLoginEmail = { email, pass ->
        repository.loginWithEmail(email, pass)
        Toast.makeText(context, "Logged in via Email!", Toast.LENGTH_SHORT).show()
      },
      onSignupEmail = { name, email, pass ->
        repository.signupWithEmail(name, email, pass)
        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
      },
      onLoginAsCeo = {
        repository.loginAsCeo()
        Toast.makeText(context, "Logged in as CEO Bir Pujara!", Toast.LENGTH_SHORT).show()
      },
      onContinueAsGuest = {
        repository.loginWithEmail("guest@bhakari.np", "guest")
      }
    )
    return
  }

  // Handle Android Back Navigation
  if (currentTab != ScreenTab.FEED) {
    BackHandler {
      currentTab = ScreenTab.FEED
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (currentTab != ScreenTab.CEO_PROFILE && currentTab != ScreenTab.CREATE_POST) {
        BhakariTopBar(
          currentLanguage = currentLanguage,
          onToggleLanguage = { repository.toggleLanguage() },
          onOpenInstallDialog = { showInstallDialog = true },
          onOpenCeoProfile = { currentTab = ScreenTab.CEO_PROFILE }
        )
      }
    },
    bottomBar = {
      if (currentTab != ScreenTab.CREATE_POST && currentTab != ScreenTab.CEO_PROFILE) {
        BhakariBottomNavigationBar(
          currentTab = currentTab,
          currentLanguage = currentLanguage,
          onSelectTab = { currentTab = it }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        ScreenTab.FEED -> {
          FeedScreen(
            posts = posts,
            currentUser = currentUser,
            ceoUser = repository.ceoUser,
            currentLanguage = currentLanguage,
            onLikePost = { repository.toggleLikePost(it) },
            onOpenComments = { postForComments = it },
            onOpenShare = { postForShare = it },
            onBluetoothShare = { post ->
              postForShare = post
              currentTab = ScreenTab.BLUETOOTH
            },
            onOpenCeoProfile = { currentTab = ScreenTab.CEO_PROFILE },
            onNavigateToCreatePost = { currentTab = ScreenTab.CREATE_POST },
            onOpenInstallDialog = { showInstallDialog = true }
          )
        }

        ScreenTab.MESSAGES -> {
          MessagesScreen(
            currentLanguage = currentLanguage,
            currentUser = currentUser,
            ceoUser = repository.ceoUser,
            chatMessages = chatMessages,
            onSendMessage = { recipientId, text ->
              repository.sendMessage(recipientId, text)
            },
            onOpenCeoProfile = { currentTab = ScreenTab.CEO_PROFILE }
          )
        }

        ScreenTab.CREATE_POST -> {
          CreatePostScreen(
            currentUser = currentUser,
            currentLanguage = currentLanguage,
            onPublish = { content, mediaType, tag ->
              repository.createPost(content, mediaType, tag)
            },
            onBack = { currentTab = ScreenTab.FEED }
          )
        }

        ScreenTab.BLUETOOTH -> {
          BluetoothShareScreen(
            nearbyPeers = nearbyPeers,
            isScanning = isScanning,
            isDiscoverable = isDiscoverable,
            currentLanguage = currentLanguage,
            currentUser = currentUser,
            latestPost = postForShare ?: posts.firstOrNull(),
            lastNotice = lastTransferNotice,
            onStartScan = { repository.startBluetoothScan() },
            onToggleDiscoverable = { repository.toggleDiscoverable() },
            onSharePostWithPeer = { peerId, post ->
              repository.sharePostOverBluetooth(peerId, post) { success ->
                if (success) {
                  Toast.makeText(
                    context,
                    LanguageStrings.get("bluetooth_sent_success", currentLanguage),
                    Toast.LENGTH_SHORT
                  ).show()
                }
              }
            },
            onShareContactWithPeer = { peerId ->
              repository.shareContactOverBluetooth(peerId) { success ->
                if (success) {
                  Toast.makeText(
                    context,
                    LanguageStrings.get("bluetooth_sent_success", currentLanguage),
                    Toast.LENGTH_SHORT
                  ).show()
                }
              }
            },
            onBack = { currentTab = ScreenTab.FEED }
          )
        }

        ScreenTab.PROFILE -> {
          UserProfileScreen(
            currentUser = currentUser,
            ceoUser = repository.ceoUser,
            currentLanguage = currentLanguage,
            onToggleLanguage = { repository.toggleLanguage() },
            onOpenCeoProfile = { currentTab = ScreenTab.CEO_PROFILE },
            onOpenInstallDialog = { showInstallDialog = true },
            onSwitchToCeo = {
              repository.loginAsCeo()
              Toast.makeText(context, "Switched to CEO Bir Pujara!", Toast.LENGTH_SHORT).show()
            },
            onLogout = {
              repository.logout()
            }
          )
        }

        ScreenTab.CEO_PROFILE -> {
          CeoProfileScreen(
            ceoUser = repository.ceoUser,
            ceoPosts = posts.filter { it.author.isCeo || it.isCeoAnnouncement },
            currentLanguage = currentLanguage,
            onBack = { currentTab = ScreenTab.FEED },
            onMessageCeo = { currentTab = ScreenTab.MESSAGES },
            onBluetoothShareWithCeo = { currentTab = ScreenTab.BLUETOOTH },
            onLikePost = { repository.toggleLikePost(it) },
            onOpenComments = { postForComments = it },
            onOpenShare = { postForShare = it }
          )
        }
      }

      // Dialogs
      if (showInstallDialog) {
        InstallPwaDialog(
          currentLanguage = currentLanguage,
          onDismiss = { showInstallDialog = false },
          onInstallConfirmed = { showInstallDialog = false }
        )
      }

      if (postForComments != null) {
        CommentsBottomSheet(
          post = postForComments!!,
          currentLanguage = currentLanguage,
          onDismiss = { postForComments = null },
          onAddComment = { text ->
            repository.addComment(postForComments!!.id, text)
            postForComments = repository.posts.value.find { it.id == postForComments!!.id }
          }
        )
      }

      if (postForShare != null) {
        ShareDialog(
          post = postForShare!!,
          currentLanguage = currentLanguage,
          onDismiss = { postForShare = null },
          onNavigateToBluetoothShare = {
            currentTab = ScreenTab.BLUETOOTH
          }
        )
      }
    }
  }
}

@Composable
fun BhakariBottomNavigationBar(
  currentTab: ScreenTab,
  currentLanguage: AppLanguage,
  onSelectTab: (ScreenTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp,
    modifier = modifier.testTag("bhakari_bottom_nav")
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentTab == ScreenTab.FEED,
      onClick = { onSelectTab(ScreenTab.FEED) },
      icon = {
        Icon(Icons.Default.Home, contentDescription = "Home")
      },
      label = {
        Text(
          text = LanguageStrings.get("nav_home", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.FEED) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BhakariGold,
        selectedTextColor = BhakariGold,
        indicatorColor = BhakariGold.copy(alpha = 0.15f)
      ),
      modifier = Modifier.testTag("nav_item_home")
    )

    // 2. Messages
    NavigationBarItem(
      selected = currentTab == ScreenTab.MESSAGES,
      onClick = { onSelectTab(ScreenTab.MESSAGES) },
      icon = {
        BadgedBox(
          badge = {
            Badge(containerColor = VerifiedGreen) {
              Text("1", color = Color.White, fontSize = 10.sp)
            }
          }
        ) {
          Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Messages")
        }
      },
      label = {
        Text(
          text = LanguageStrings.get("nav_messages", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.MESSAGES) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BhakariGold,
        selectedTextColor = BhakariGold,
        indicatorColor = BhakariGold.copy(alpha = 0.15f)
      ),
      modifier = Modifier.testTag("nav_item_messages")
    )

    // 3. Create Post FAB
    NavigationBarItem(
      selected = currentTab == ScreenTab.CREATE_POST,
      onClick = { onSelectTab(ScreenTab.CREATE_POST) },
      icon = {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(BhakariGold),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Add, contentDescription = "Create Post", tint = Color.Black)
        }
      },
      label = {
        Text(
          text = LanguageStrings.get("nav_create", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.CREATE_POST) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BhakariGold,
        selectedTextColor = BhakariGold,
        indicatorColor = Color.Transparent
      ),
      modifier = Modifier.testTag("nav_item_create")
    )

    // 4. Nearby Bluetooth
    NavigationBarItem(
      selected = currentTab == ScreenTab.BLUETOOTH,
      onClick = { onSelectTab(ScreenTab.BLUETOOTH) },
      icon = {
        Icon(Icons.Default.Bluetooth, contentDescription = "Nearby Bluetooth")
      },
      label = {
        Text(
          text = LanguageStrings.get("nav_bluetooth", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.BLUETOOTH) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = VerifiedGreen,
        selectedTextColor = VerifiedGreen,
        indicatorColor = VerifiedGreen.copy(alpha = 0.15f)
      ),
      modifier = Modifier.testTag("nav_item_bluetooth")
    )

    // 5. Profile
    NavigationBarItem(
      selected = currentTab == ScreenTab.PROFILE || currentTab == ScreenTab.CEO_PROFILE,
      onClick = { onSelectTab(ScreenTab.PROFILE) },
      icon = {
        Icon(Icons.Default.Person, contentDescription = "Profile")
      },
      label = {
        Text(
          text = LanguageStrings.get("nav_profile", currentLanguage),
          fontSize = 11.sp,
          fontWeight = if (currentTab == ScreenTab.PROFILE) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BhakariGold,
        selectedTextColor = BhakariGold,
        indicatorColor = BhakariGold.copy(alpha = 0.15f)
      ),
      modifier = Modifier.testTag("nav_item_profile")
    )
  }
}
