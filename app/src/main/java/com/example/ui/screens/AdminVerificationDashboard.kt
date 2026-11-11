package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.LanguageStrings
import com.example.model.AdminLog
import com.example.model.Announcement
import com.example.model.Post
import com.example.model.Report
import com.example.model.ReportStatus
import com.example.model.User
import com.example.model.VerificationRequest
import com.example.model.VerificationStatus
import com.example.ui.components.UserAvatar
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.FacebookVerifiedBlue
import com.example.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminVerificationDashboard(
  currentUser: User?,
  ceoUser: User,
  verificationRequests: List<VerificationRequest>,
  allUsers: List<User>,
  reportedPosts: List<Post>,
  announcements: List<Announcement>,
  adminLogs: List<AdminLog>,
  currentLanguage: AppLanguage,
  onApproveRequest: (requestId: String) -> Unit,
  onRejectRequest: (requestId: String, note: String) -> Unit,
  onGrantBadge: (userId: String) -> Unit,
  onRemoveBadge: (userId: String) -> Unit,
  onPromoteModerator: (userId: String) -> Unit,
  onDemoteModerator: (userId: String) -> Unit,
  onBlockUser: (userId: String) -> Unit,
  onUnblockUser: (userId: String) -> Unit,
  onSuspendUser: (userId: String) -> Unit,
  onReactivateUser: (userId: String) -> Unit,
  onRemovePost: (postId: String) -> Unit,
  onDismissReport: (postId: String) -> Unit,
  onCreateAnnouncement: (title: String, nepaliTitle: String, message: String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val isHindi = currentLanguage == AppLanguage.HINDI

  // Authorization check: CEO has full control; moderators can view moderation
  val isCeo = currentUser != null && currentUser.isCeo
  val isModerator = currentUser != null && currentUser.isModerator
  val hasAccess = isCeo || isModerator

  var selectedTab by remember { mutableIntStateOf(0) }
  var searchQuery by remember { mutableStateOf("") }
  
  // Announcement composition state
  var announcementTitle by remember { mutableStateOf("") }
  var announcementNepaliTitle by remember { mutableStateOf("") }
  var announcementMessage by remember { mutableStateOf("") }

  val pendingRequests = verificationRequests.filter { it.status == VerificationStatus.PENDING }
  val totalVerified = allUsers.count { it.hasBlueBadge }
  val pendingReports = reportedPosts.filter { it.isReported && !it.isRemoved }

  val filteredUsers = if (searchQuery.isBlank()) {
    allUsers
  } else {
    allUsers.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
      it.nepaliName.contains(searchQuery) ||
      it.username.contains(searchQuery, ignoreCase = true)
    }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .testTag("admin_verification_dashboard")
    ) {
      // Top App Bar
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = LanguageStrings.get("admin_dashboard", currentLanguage),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Spacer(modifier = Modifier.width(6.dp))
              VerifiedBadge(size = 17.dp, isCeo = true)
            }
            Text(
              text = if (isCeo) "Authorized Admin: Bir Pujara (CEO)" else "Staff: Community Moderator",
              style = MaterialTheme.typography.labelSmall.copy(
                color = FacebookVerifiedBlue,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("admin_dashboard_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )

      // Security Access Denied Card if regular user
      if (!hasAccess) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = BhakariCrimson.copy(alpha = 0.1f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = BhakariCrimson, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isNepali) "पहुँच अस्वीकृत (Unauthorized)" else "Access Denied",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BhakariCrimson)
              )
              Text(
                text = if (isNepali) "यो प्रशासनिक ड्यासबोर्ड सिईओ वीर पुजारा र नियुक्त मोडरेटरहरूको लागि मात्र हो।"
                else "This administrative dashboard is restricted to CEO Bir Pujara and appointed moderators.",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
              )
            }
          }
        }
        return@Column
      }

      // Statistics Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        StatItem(label = "Verified", value = totalVerified.toString(), color = FacebookVerifiedBlue, modifier = Modifier.weight(1f))
        StatItem(label = "Requests", value = pendingRequests.size.toString(), color = BhakariGold, modifier = Modifier.weight(1f))
        StatItem(label = "Reports", value = pendingReports.size.toString(), color = BhakariCrimson, modifier = Modifier.weight(1f))
        StatItem(label = "Users", value = allUsers.size.toString(), color = VerifiedGreen, modifier = Modifier.weight(1f))
      }

      // Tabs: Applications, Directory & Roles, Moderation, Announcements, Audit Logs
      val tabs = listOf(
        "Verification (${pendingRequests.size})",
        "Users & Roles",
        "Moderation (${pendingReports.size})",
        "Announce",
        "Audit Log"
      )

      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = FacebookVerifiedBlue,
        edgePadding = 12.dp,
        indicator = { tabPositions ->
          if (selectedTab < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier
                .tabIndicatorOffset(tabPositions[selectedTab])
                .clip(RoundedCornerShape(4.dp)),
              color = FacebookVerifiedBlue,
              height = 3.dp
            )
          }
        }
      ) {
        tabs.forEachIndexed { index, tabName ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(text = tabName, fontWeight = FontWeight.Bold, fontSize = 13.sp) },
            modifier = Modifier.testTag("admin_tab_$index")
          )
        }
      }

      when (selectedTab) {
        0 -> {
          // Tab 0: Verification Applications
          if (pendingRequests.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Check, contentDescription = null, tint = FacebookVerifiedBlue, modifier = Modifier.size(52.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = if (isNepali) "सबै आवेदनहरू समीक्षा भइसके!" else "No Pending Requests",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = if (isNepali) "नयाँ प्रमाणीकरण आवेदन आएपछि यहाँ देखिनेछ।" else "New verification applications will appear here.",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }
          } else {
            LazyColumn(
              modifier = Modifier
                .fillMaxSize()
                .testTag("pending_requests_list"),
              contentPadding = PaddingValues(14.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              items(pendingRequests, key = { it.id }) { request ->
                Card(
                  shape = RoundedCornerShape(18.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      UserAvatar(user = request.user, size = 44.dp)
                      Spacer(modifier = Modifier.width(10.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(text = request.user.name, fontWeight = FontWeight.Bold)
                          Spacer(modifier = Modifier.width(4.dp))
                          VerifiedBadge(size = 14.dp, isCeo = false)
                        }
                        Text(
                          text = "@${request.user.username} • ${request.submittedAt}",
                          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = "Category: ${request.category} • Doc: ${request.documentType}",
                      style = MaterialTheme.typography.labelSmall.copy(color = FacebookVerifiedBlue, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = request.reason,
                      style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                      OutlinedButton(
                        onClick = { onRejectRequest(request.id, "Guidelines not met") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BhakariCrimson),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                      ) {
                        Text(LanguageStrings.get("reject_btn", currentLanguage))
                      }
                      Button(
                        onClick = { onApproveRequest(request.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.5f)
                      ) {
                        Text(LanguageStrings.get("approve_btn", currentLanguage), fontWeight = FontWeight.Bold)
                      }
                    }
                  }
                }
              }
            }
          }
        }

        1 -> {
          // Tab 1: User Directory & Role Management
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp)
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = { Text(if (isNepali) "प्रयोगकर्ता खोज्नुहोस्..." else "Search user to manage...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              shape = RoundedCornerShape(14.dp),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              contentPadding = PaddingValues(bottom = 30.dp),
              modifier = Modifier.fillMaxSize()
            ) {
              items(filteredUsers, key = { it.id }) { user ->
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      UserAvatar(user = user, size = 42.dp)
                      Spacer(modifier = Modifier.width(10.dp))
                      Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(text = user.name, fontWeight = FontWeight.Bold)
                          if (user.hasBlueBadge) {
                            Spacer(modifier = Modifier.width(4.dp))
                            VerifiedBadge(size = 14.dp, isCeo = user.isCeo)
                          }
                        }
                        Text(
                          text = "@${user.username} • Role: ${user.role.uppercase()}",
                          style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        if (user.isBlocked) {
                          Text(text = "🚨 BLOCKED", color = BhakariCrimson, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        } else if (user.isSuspended) {
                          Text(text = "⚠️ SUSPENDED", color = BhakariGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!user.isCeo && isCeo) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                      ) {
                        // Badge toggle
                        if (user.hasBlueBadge) {
                          OutlinedButton(
                            onClick = { onRemoveBadge(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("Remove Badge", fontSize = 11.sp, color = BhakariCrimson)
                          }
                        } else {
                          Button(
                            onClick = { onGrantBadge(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("Grant Badge", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                          }
                        }

                        // Moderator toggle
                        if (user.isModerator) {
                          OutlinedButton(
                            onClick = { onDemoteModerator(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("Demote Mod", fontSize = 11.sp)
                          }
                        } else {
                          OutlinedButton(
                            onClick = { onPromoteModerator(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("+ Moderator", fontSize = 11.sp, color = VerifiedGreen)
                          }
                        }

                        // Block/Suspend toggle
                        if (user.isBlocked) {
                          Button(
                            onClick = { onUnblockUser(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("Unblock", fontSize = 11.sp)
                          }
                        } else {
                          OutlinedButton(
                            onClick = { onBlockUser(user.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                          ) {
                            Text("Block", fontSize = 11.sp, color = BhakariCrimson)
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }

        2 -> {
          // Tab 2: Moderation & Reports Queue
          if (pendingReports.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(52.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "No Reported Content", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "The platform feed is clean and compliant.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
              }
            }
          } else {
            LazyColumn(
              contentPadding = PaddingValues(14.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.fillMaxSize()
            ) {
              items(pendingReports, key = { it.id }) { post ->
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Report, contentDescription = null, tint = BhakariCrimson)
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(text = "Reported Post by @${post.author.username}", fontWeight = FontWeight.Bold, color = BhakariCrimson)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = post.content,
                      style = MaterialTheme.typography.bodyMedium,
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(8.dp)
                    )
                    if (post.reportReason.isNotBlank()) {
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(text = "Reason: ${post.reportReason}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                      OutlinedButton(
                        onClick = { onDismissReport(post.id) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                      ) {
                        Text(LanguageStrings.get("dismiss_report", currentLanguage))
                      }
                      Button(
                        onClick = { onRemovePost(post.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = BhakariCrimson),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.5f)
                      ) {
                        Text(LanguageStrings.get("remove_post", currentLanguage), fontWeight = FontWeight.Bold)
                      }
                    }
                  }
                }
              }
            }
          }
        }

        3 -> {
          // Tab 3: Broadcast Announcements
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(14.dp)
          ) {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Campaign, contentDescription = null, tint = FacebookVerifiedBlue)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Broadcast Announcement to All Users",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = announcementTitle,
                  onValueChange = { announcementTitle = it },
                  label = { Text("Title (English)") },
                  shape = RoundedCornerShape(12.dp),
                  singleLine = true,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = announcementNepaliTitle,
                  onValueChange = { announcementNepaliTitle = it },
                  label = { Text("शीर्षक (नेपाली)") },
                  shape = RoundedCornerShape(12.dp),
                  singleLine = true,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = announcementMessage,
                  onValueChange = { announcementMessage = it },
                  label = { Text("Announcement Message") },
                  shape = RoundedCornerShape(12.dp),
                  minLines = 3,
                  maxLines = 5,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                  onClick = {
                    if (announcementTitle.isNotBlank() && announcementMessage.isNotBlank()) {
                      onCreateAnnouncement(
                        announcementTitle,
                        announcementNepaliTitle.ifBlank { announcementTitle },
                        announcementMessage
                      )
                      announcementTitle = ""
                      announcementNepaliTitle = ""
                      announcementMessage = ""
                      Toast.makeText(context, LanguageStrings.get("announcement_sent_toast", currentLanguage), Toast.LENGTH_SHORT).show()
                    } else {
                      Toast.makeText(context, "Please enter title and message", Toast.LENGTH_SHORT).show()
                    }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Publish Announcement", fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Published Announcements", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            announcements.forEach { ann ->
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(text = if (isNepali) ann.nepaliTitle else ann.title, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = if (isNepali) ann.nepaliMessage else ann.message, style = MaterialTheme.typography.bodySmall)
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = "By ${ann.authorName} • ${ann.createdAt}", style = MaterialTheme.typography.labelSmall.copy(color = FacebookVerifiedBlue))
                }
              }
            }
          }
        }

        4 -> {
          // Tab 4: Admin Audit Log
          LazyColumn(
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
          ) {
            items(adminLogs, key = { it.id }) { log ->
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(FacebookVerifiedBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = FacebookVerifiedBlue, modifier = Modifier.size(18.dp))
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(text = "${log.action} • ${log.adminName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = log.details, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text(text = log.timestamp, style = MaterialTheme.typography.labelSmall.copy(color = FacebookVerifiedBlue, fontSize = 10.sp))
                  }
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
private fun StatItem(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color))
      Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), maxLines = 1)
    }
  }
}
