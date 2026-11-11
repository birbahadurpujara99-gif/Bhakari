package com.example.data

import android.content.Context
import com.example.R
import com.example.model.AdminLog
import com.example.model.Announcement
import com.example.model.BluetoothDevicePeer
import com.example.model.ChatMessage
import com.example.model.Comment
import com.example.model.MediaType
import com.example.model.Post
import com.example.model.Report
import com.example.model.ReportStatus
import com.example.model.User
import com.example.model.VerificationRequest
import com.example.model.VerificationStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BhakariRepository(context: Context? = null) {

  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  // Cloud Firestore database connection using custom database ID
  private val firestoreDb: FirebaseFirestore? = try {
    if (context != null) {
      FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
    } else {
      null
    }
  } catch (e: Exception) {
    null
  }

  // CEO Bir Pujara - Official Account with Permanent Blue Verified Badge & Full Admin Control
  val ceoUser = User(
    id = "ceo_bir_pujara",
    name = "Bir Pujara",
    nepaliName = "वीर पुजारा",
    hindiName = "बीर पुजारा",
    username = "birpujara_ceo",
    avatarRes = R.drawable.ceo_bir_pujara,
    avatarColorHex = 0xFF10B981,
    isVerified = true,
    isBlueVerified = true,
    isCeo = true,
    role = "ceo",
    title = "Founder & CEO, Bhakari",
    nepaliTitle = "संस्थापक तथा सिईओ, भकारी",
    hindiTitle = "संस्थापक एवं सीईओ, भकारी",
    bio = "CEO & Founder of Bhakari Social Media. Connecting communities worldwide with our secure platform & Bluetooth offline sharing! 🇳🇵🌾",
    nepaliBio = "भकारी सामाजिक सञ्जालका संस्थापक तथा सिईओ। विश्वभरका नेपालीहरूलाई जोड्दै, सुरक्षित प्रविधि र ब्लुटुथ सेयरिङका साथ! गर्वका साथ नेपाली! 🇳🇵🌾",
    hindiBio = "भकारी सोशल मीडिया के संस्थापक एवं सीईओ। विश्वभर के समुदायों को सुरक्षित तकनीक से जोड़ते हुए! 🌾",
    email = "birbahadurpujara99@gmail.com",
    phone = "+977 9801234567",
    followersCount = 128450,
    followingCount = 120,
    postsCount = 48,
    isFollowedByMe = true
  )

  // Normal Community Users
  val userAarav = User(
    id = "user_aarav",
    name = "Aarav Sharma",
    nepaliName = "आरभ शर्मा",
    hindiName = "आरव शर्मा",
    username = "aarav_nepal",
    avatarColorHex = 0xFF3B82F6,
    isVerified = true,
    isBlueVerified = true,
    role = "moderator", // Appointed moderator
    title = "Traveler & Community Moderator",
    nepaliTitle = "यात्री तथा समुदाय मोडरेटर",
    bio = "Capturing the beauty of the Himalayas 🏔️",
    nepaliBio = "हिमालको सुन्दरता क्यामेरामा कैद गर्दै 🏔️",
    followersCount = 14200,
    followingCount = 350,
    postsCount = 112
  )

  val userPrakriti = User(
    id = "user_prakriti",
    name = "Prakriti Shrestha",
    nepaliName = "प्रकृति श्रेष्ठ",
    hindiName = "प्रकृति श्रेष्ठ",
    username = "prakriti_vibes",
    avatarColorHex = 0xFFEC4899,
    isVerified = false,
    isBlueVerified = false,
    role = "user",
    title = "Cultural Creator",
    nepaliTitle = "सांस्कृतिक सिर्जनाकर्ता",
    bio = "Celebrating Nepali art, songs, and food! 🥟🪔",
    nepaliBio = "नेपाली कला, गीत र परिकारको उत्सव! 🥟🪔",
    followersCount = 8900,
    followingCount = 420,
    postsCount = 64
  )

  val userSunita = User(
    id = "user_sunita",
    name = "Sunita KC",
    nepaliName = "सुनिता केसी",
    hindiName = "सुनीता केसी",
    username = "sunita_news",
    avatarColorHex = 0xFF8B5CF6,
    isVerified = false,
    isBlueVerified = false,
    role = "user",
    title = "Independent Journalist",
    nepaliTitle = "स्वतन्त्र पत्रकार",
    bio = "Covering stories from rural Nepal. Truth & voice! 📰🎙️",
    nepaliBio = "नेपालका ग्रामीण कथा र सत्य आवाज उजागर गर्दै! 📰🎙️",
    followersCount = 18400,
    followingCount = 210,
    postsCount = 95
  )

  val userDeepak = User(
    id = "user_deepak",
    name = "Deepak Bajracharya",
    nepaliName = "दीपक बज्राचार्य",
    hindiName = "दीपक बज्राचार्य",
    username = "deepak_music",
    avatarColorHex = 0xFFEF4444,
    isVerified = true,
    isBlueVerified = true,
    role = "user",
    title = "Musician & Performer",
    nepaliTitle = "सङ्गीतकार तथा कलाकार",
    bio = "Nepali melodies, contemporary beats 🎸🎶",
    nepaliBio = "नेपाली सुमधुर धुन र सङ्गीत 🎸🎶",
    followersCount = 92000,
    followingCount = 180,
    postsCount = 140
  )

  // Current Logged In User State
  private val _currentUser = MutableStateFlow<User?>(
    User(
      id = "me_suman",
      name = "Suman Thapa",
      nepaliName = "सुमन थापा",
      hindiName = "सुमन थापा",
      username = "suman_thapa",
      avatarColorHex = 0xFFF59E0B,
      isVerified = false,
      isBlueVerified = false,
      role = "user",
      title = "Bhakari Member",
      nepaliTitle = "भकारी सदस्य",
      bio = "Tech enthusiast from Kathmandu. Loving the new Bhakari app! 🚀",
      nepaliBio = "काठमाडौंबाट प्रविधि प्रेमी। नयाँ भकारी एप निकै मन पर्यो! 🚀",
      email = "suman.thapa@bhakari.np",
      phone = "+977 9841000000",
      followersCount = 420,
      followingCount = 180,
      postsCount = 12
    )
  )
  val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

  // App Language State: Support English, Nepali, and Hindi
  private val _currentLanguage = MutableStateFlow(AppLanguage.NEPALI)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  fun toggleLanguage() {
    _currentLanguage.value = _currentLanguage.value.next()
  }

  fun setLanguage(lang: AppLanguage) {
    _currentLanguage.value = lang
  }

  // Posts Feed State
  private val _posts = MutableStateFlow<List<Post>>(emptyList())
  val posts: StateFlow<List<Post>> = _posts.asStateFlow()

  // Chat Conversations State
  private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
  val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

  // Bluetooth Nearby Peers State
  private val _nearbyPeers = MutableStateFlow<List<BluetoothDevicePeer>>(emptyList())
  val nearbyPeers: StateFlow<List<BluetoothDevicePeer>> = _nearbyPeers.asStateFlow()

  private val _isBluetoothScanning = MutableStateFlow(false)
  val isBluetoothScanning: StateFlow<Boolean> = _isBluetoothScanning.asStateFlow()

  private val _isDiscoverable = MutableStateFlow(true)
  val isDiscoverable: StateFlow<Boolean> = _isDiscoverable.asStateFlow()

  private val _lastTransferNotice = MutableStateFlow<String?>(null)
  val lastTransferNotice: StateFlow<String?> = _lastTransferNotice.asStateFlow()

  // All Users Directory (For Community & Badge Control)
  private val _allUsers = MutableStateFlow<List<User>>(emptyList())
  val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

  // Verification Requests State (For CEO Bir Pujara Admin Dashboard)
  private val _verificationRequests = MutableStateFlow<List<VerificationRequest>>(emptyList())
  val verificationRequests: StateFlow<List<VerificationRequest>> = _verificationRequests.asStateFlow()

  // Reports Queue State (For CEO and Moderators)
  private val _reports = MutableStateFlow<List<Report>>(emptyList())
  val reports: StateFlow<List<Report>> = _reports.asStateFlow()

  // Platform Announcements State
  private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
  val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

  // Admin Activity Log State
  private val _adminLogs = MutableStateFlow<List<AdminLog>>(emptyList())
  val adminLogs: StateFlow<List<AdminLog>> = _adminLogs.asStateFlow()

  init {
    initSeedData()
    syncWithFirestore()
  }

  private fun initSeedData() {
    val initialComments = listOf(
      Comment(
        id = "c1",
        author = userAarav,
        text = "Heartiest congratulations to Bir Pujara and team Bhakari! Proud moment for Nepal! 🇳🇵✨",
        nepaliText = "वीर पुजारा र सम्पूर्ण भकारी टिमलाई हार्दिक बधाई! नेपालको लागि गर्वको क्षण! 🇳🇵✨",
        timestamp = "10m ago",
        likesCount = 42
      ),
      Comment(
        id = "c2",
        author = userPrakriti,
        text = "The Bluetooth nearby sharing feature without internet is truly revolutionary for our rural areas!",
        nepaliText = "इन्टरनेट नहुँदा पनि चल्ने ब्लुटुथ सेयरिङ सुविधा हाम्रा ग्रामीण भेगका लागि साँच्चिकै उपयोगी छ!",
        timestamp = "5m ago",
        likesCount = 28
      )
    )

    val seedPosts = listOf(
      Post(
        id = "post_ceo_announcement",
        author = ceoUser,
        timestamp = "Just now",
        content = "Namaste everyone! 🌾 As Founder & CEO, I am thrilled to welcome you to Bhakari. Our platform now features real-time Facebook-style blue verification badges (🔵✓), complete administrative oversight, and offline Bluetooth radar sharing. Proudly connecting communities! 🇳🇵✨",
        nepaliContent = "नमस्ते सम्पूर्णमा! 🌾 भकारीमा सबैलाई हार्दिक स्वागत छ। हाम्रो प्लेटफर्ममा अब फेसबुक-शैलीको निलो प्रमाणित टिक (🔵✓), सिईओ प्रशासनिक नियन्त्रण र अफलाइन ब्लुटुथ सेयरिङ पूर्ण रूपमा सञ्चालनमा आएको छ। गर्वका साथ नेपाली! 🇳🇵✨",
        hindiContent = "नमस्ते सभी को! 🌾 भकारी में आप सभी का हार्दिक स्वागत है। हमारे प्लेटफॉर्म पर अब फेसबुक-स्टाइल नीला वेरिफिकेशन टिक (🔵✓) और ऑफलाइन ब्लूटूथ शेयरिंग उपलब्ध है।",
        mediaType = MediaType.PHOTO,
        imageRes = R.drawable.bhakari_cover_art,
        tags = listOf("#BhakariPlatform", "#BirPujara", "#BlueVerified", "#NepaliInnovation"),
        likesCount = 4120,
        commentsCount = 280,
        sharesCount = 610,
        isLiked = true,
        comments = initialComments,
        isCeoAnnouncement = true,
        location = "Kathmandu, Nepal"
      ),
      Post(
        id = "post_video_tihar",
        author = userPrakriti,
        timestamp = "2 hours ago",
        content = "Celebration of lights and community songs in Bhaktapur Durbar Square! Watch how beautifully the community gathered tonight. 🪔✨",
        nepaliContent = "भक्तपुर दरबार क्षेत्रमा दीप प्रज्वलन र मौलिक देउसी-भैलो गीतको रौनक! हेर्नुहोस् समुदाय कति सुन्दर ढङ्गले एकजुट भएको छ। 🪔✨",
        mediaType = MediaType.VIDEO,
        videoThumbnailRes = R.drawable.bhakari_cover_art,
        videoTitle = "Tihar Festival Cultural Dance & Diyo Lights",
        tags = listOf("#NepalCulture", "#Bhaktapur", "#FestivalsOfNepal", "#BhakariMoments"),
        likesCount = 1240,
        commentsCount = 89,
        sharesCount = 142,
        isLiked = false,
        comments = emptyList(),
        location = "Bhaktapur, Nepal"
      ),
      Post(
        id = "post_aarav_nature",
        author = userAarav,
        timestamp = "5 hours ago",
        content = "The majesty of Machhapuchhre reflected in the calm waters of Phewa Lake, Pokhara. Nature has its own way of bringing peace. 🏔️🚣",
        nepaliContent = "फेवातालको शान्त पानीमा देखिएको माछापुच्छ्रे हिमालको मनोरम दृश्य। प्रकृतिले दिने शान्ति अद्वितीय छ। 🏔️🚣",
        mediaType = MediaType.TEXT_ONLY,
        tags = listOf("#Pokhara", "#Annapurna", "#VisitNepal", "#Himalayas"),
        likesCount = 980,
        commentsCount = 45,
        sharesCount = 68,
        isLiked = false,
        comments = emptyList(),
        location = "Pokhara, Nepal"
      )
    )

    _posts.value = seedPosts

    // Initial Users
    val currentUserObj = _currentUser.value
    _allUsers.value = listOfNotNull(
      ceoUser,
      currentUserObj,
      userAarav,
      userPrakriti,
      userSunita,
      userDeepak
    )

    // Initial Verification Applications
    _verificationRequests.value = listOf(
      VerificationRequest(
        id = "req_prakriti_01",
        user = userPrakriti,
        category = "Cultural Creator / Artist",
        nepaliCategory = "सांस्कृतिक सिर्जनाकर्ता / कलाकार",
        documentType = "National Identity Card (राष्ट्रिय परिचयपत्र)",
        reason = "Promoting traditional folk songs and heritage. Reached 8.9k authentic community followers.",
        knownAs = "Prakriti Vibes",
        websiteOrLink = "https://bhakari.np/@prakriti_vibes",
        status = VerificationStatus.PENDING,
        submittedAt = "Today at 09:15 AM"
      ),
      VerificationRequest(
        id = "req_sunita_02",
        user = userSunita,
        category = "Journalist / News Reporter",
        nepaliCategory = "पत्रकार / समाचार सम्प्रेषक",
        documentType = "Press Council Nepal Card",
        reason = "Independent journalist covering local community development in Karnali.",
        knownAs = "Sunita KC",
        websiteOrLink = "https://presscouncilnepal.gov.np/reporters/sunita-kc",
        status = VerificationStatus.PENDING,
        submittedAt = "Yesterday at 04:30 PM"
      )
    )

    // Initial Reports for Moderation Review
    _reports.value = listOf(
      Report(
        id = "rep_01",
        reporterUserId = "user_aarav",
        reporterName = "Aarav Sharma",
        reportedType = "comment",
        targetId = "c_spam_99",
        targetContentSnippet = "Suspicious financial spam link detected in public comments",
        reason = "Spam / Unauthorized advertisement link",
        status = ReportStatus.PENDING,
        createdAt = "1 hour ago"
      )
    )

    // Initial Announcements
    _announcements.value = listOf(
      Announcement(
        id = "ann_01",
        title = "Official Launch of Bhakari Blue Verification & Moderation",
        nepaliTitle = "भकारी निलो ब्याच तथा मोडरेसन प्रणालीको शुभारम्भ",
        message = "All creators and public figures can now submit verification requests directly from profile settings. Verified badges are reviewed and granted by CEO Bir Pujara.",
        nepaliMessage = "अब सबै सिर्जनाकर्ता र व्यक्तित्वहरूले प्रोफाइल सेटिङबाट सिधै निलो प्रमाणित ब्याचका लागि आवेदन दिन सक्नुहुन्छ।",
        authorName = "Bir Pujara (CEO)",
        createdAt = "Today at 10:00 AM"
      )
    )

    // Initial Admin Logs
    _adminLogs.value = listOf(
      AdminLog(
        id = "log_01",
        adminName = "Bir Pujara (CEO)",
        action = "Platform Launched",
        details = "CEO Bir Pujara initiated Bhakari platform with verified badge controls",
        timestamp = "Today at 09:00 AM"
      ),
      AdminLog(
        id = "log_02",
        adminName = "Bir Pujara (CEO)",
        action = "Promoted Moderator",
        details = "Promoted Aarav Sharma (@aarav_nepal) to community moderator",
        timestamp = "Today at 09:30 AM"
      )
    )

    // Initial Nearby Bluetooth Peers
    _nearbyPeers.value = listOf(
      BluetoothDevicePeer(
        id = "bt_ceo_device",
        name = "Bir Pujara's Flagship (CEO)",
        deviceAddress = "74:D2:1D:9C:B1:01",
        rssi = -42,
        isBhakariUser = true,
        associatedUser = ceoUser
      ),
      BluetoothDevicePeer(
        id = "bt_aarav_pixel",
        name = "Aarav's Pixel 9 Pro",
        deviceAddress = "88:3C:99:12:4F:22",
        rssi = -58,
        isBhakariUser = true,
        associatedUser = userAarav
      ),
      BluetoothDevicePeer(
        id = "bt_prakriti_galaxy",
        name = "Prakriti's Galaxy",
        deviceAddress = "C4:A5:66:88:99:AA",
        rssi = -64,
        isBhakariUser = true,
        associatedUser = userPrakriti
      )
    )
  }

  private fun syncWithFirestore() {
    if (firestoreDb == null) return
    coroutineScope.launch(Dispatchers.IO) {
      try {
        // Sync CEO User profile to Firestore to guarantee permanent CEO account
        firestoreDb.collection("users").document(ceoUser.id).set(ceoUser)
      } catch (e: Exception) {
        // Safe fallback
      }
    }
  }

  // Edit and Save User Profile Permanently
  fun updateUserProfile(updatedUser: User) {
    _currentUser.value = updatedUser
    _allUsers.value = _allUsers.value.map { if (it.id == updatedUser.id) updatedUser else it }
    _posts.value = _posts.value.map { post ->
      if (post.author.id == updatedUser.id) post.copy(author = updatedUser) else post
    }
    // Save to Firestore
    if (firestoreDb != null) {
      coroutineScope.launch(Dispatchers.IO) {
        try {
          firestoreDb.collection("users").document(updatedUser.id).set(updatedUser)
        } catch (e: Exception) {
          // Fallback
        }
      }
    }
  }

  fun updateUserProfile(
    name: String,
    username: String,
    bio: String,
    avatarUri: String? = null,
    coverUri: String? = null,
    isPrivate: Boolean = false,
    allowMessages: Boolean = true,
    allowBluetooth: Boolean = true
  ) {
    val current = _currentUser.value ?: return
    val updated = current.copy(
      name = name,
      username = username,
      bio = bio,
      avatarUrl = if (!avatarUri.isNullOrBlank()) avatarUri else current.avatarUrl,
      coverUrl = if (!coverUri.isNullOrBlank()) coverUri else current.coverUrl,
      isPrivateAccount = isPrivate,
      allowMessagesFromAnyone = allowMessages,
      allowBluetoothDiscovery = allowBluetooth
    )
    updateUserProfile(updated)
  }

  // Delete User Account
  fun deleteAccount(userId: String = _currentUser.value?.id ?: "") {
    if (userId.isBlank()) return
    _allUsers.value = _allUsers.value.filterNot { it.id == userId }
    _posts.value = _posts.value.filterNot { it.author.id == userId }
    if (_currentUser.value?.id == userId) {
      _currentUser.value = null
    }
    if (firestoreDb != null) {
      coroutineScope.launch(Dispatchers.IO) {
        try {
          firestoreDb.collection("users").document(userId).delete()
        } catch (e: Exception) {
          // Fallback
        }
      }
    }
  }

  // Like / Unlike Post
  fun toggleLikePost(postId: String) {
    _posts.value = _posts.value.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
        post.copy(isLiked = newLiked, likesCount = newCount)
      } else {
        post
      }
    }
  }

  // Add Comment to Post
  fun addComment(postId: String, text: String) {
    val author = _currentUser.value ?: userAarav
    val newComment = Comment(
      id = "c_${System.currentTimeMillis()}",
      author = author,
      text = text,
      nepaliText = text,
      timestamp = "Just now",
      likesCount = 0
    )
    _posts.value = _posts.value.map { post ->
      if (post.id == postId) {
        post.copy(
          comments = listOf(newComment) + post.comments,
          commentsCount = post.commentsCount + 1
        )
      } else {
        post
      }
    }
  }

  // Share Post increment
  fun sharePost(postId: String) {
    _posts.value = _posts.value.map { post ->
      if (post.id == postId) post.copy(sharesCount = post.sharesCount + 1) else post
    }
  }

  // Create Post
  fun createPost(content: String, mediaType: MediaType, tag: String = "#Bhakari") {
    val author = _currentUser.value ?: ceoUser
    val newPost = Post(
      id = "post_${System.currentTimeMillis()}",
      author = author,
      timestamp = "Just now",
      content = content,
      nepaliContent = content,
      mediaType = mediaType,
      imageRes = if (mediaType == MediaType.PHOTO) R.drawable.bhakari_cover_art else null,
      tags = listOf(tag, "#BhakariCommunity"),
      likesCount = 1,
      commentsCount = 0,
      sharesCount = 0,
      isLiked = true,
      comments = emptyList(),
      isCeoAnnouncement = author.isCeo,
      location = "Nepal"
    )
    _posts.value = listOf(newPost) + _posts.value

    if (firestoreDb != null) {
      coroutineScope.launch(Dispatchers.IO) {
        try {
          firestoreDb.collection("posts").document(newPost.id).set(newPost)
        } catch (e: Exception) {
          // Fallback
        }
      }
    }
  }

  // Send Message in Chat
  fun sendMessage(recipientId: String, text: String) {
    val sender = _currentUser.value ?: ceoUser
    val newMessage = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      senderId = sender.id,
      recipientId = recipientId,
      text = text,
      nepaliText = text,
      timestamp = "Just now",
      isFromMe = true
    )
    val currentHistory = _chatMessages.value[recipientId] ?: emptyList()
    _chatMessages.value = _chatMessages.value + (recipientId to (currentHistory + newMessage))
  }

  // Bluetooth Radar Controls
  fun startBluetoothScan() {
    _isBluetoothScanning.value = true
  }

  fun toggleDiscoverable() {
    _isDiscoverable.value = !_isDiscoverable.value
  }

  fun sharePostOverBluetooth(peerId: String, post: Post, onComplete: (Boolean) -> Unit) {
    val peer = _nearbyPeers.value.find { it.id == peerId }
    _lastTransferNotice.value = "Post shared with ${peer?.name ?: "nearby peer"} via Bluetooth!"
    onComplete(true)
  }

  fun shareContactOverBluetooth(peerId: String, onComplete: (Boolean) -> Unit) {
    val peer = _nearbyPeers.value.find { it.id == peerId }
    val user = _currentUser.value ?: ceoUser
    _lastTransferNotice.value = "Contact Card (${user.name}) shared with ${peer?.name ?: "nearby peer"}!"
    onComplete(true)
  }

  // Verification Application Submission by User
  fun submitVerificationRequest(
    category: String,
    nepaliCategory: String = "",
    docType: String,
    reason: String,
    knownAs: String = "",
    link: String = ""
  ): Boolean {
    val user = _currentUser.value ?: return false
    val newReq = VerificationRequest(
      id = "req_${System.currentTimeMillis()}",
      user = user,
      category = category,
      nepaliCategory = nepaliCategory,
      documentType = docType,
      reason = reason,
      knownAs = knownAs,
      websiteOrLink = link,
      status = VerificationStatus.PENDING,
      submittedAt = "Just now"
    )
    _verificationRequests.value = listOf(newReq) + _verificationRequests.value

    if (firestoreDb != null) {
      coroutineScope.launch(Dispatchers.IO) {
        try {
          firestoreDb.collection("verification_requests").document(newReq.id).set(newReq)
        } catch (e: Exception) {
          // Fallback
        }
      }
    }
    return true
  }

  fun submitVerificationRequest(reason: String, websiteOrLink: String = "", category: String = "Creator"): Boolean {
    return submitVerificationRequest(category, "", "ID_CARD", reason, "", websiteOrLink)
  }

  // CEO Bir Pujara: Approve Verification Request & Grant Blue Badge
  fun approveVerificationRequest(requestId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false // Security check: Only CEO Bir Pujara
    val targetReq = _verificationRequests.value.find { it.id == requestId } ?: return false

    _verificationRequests.value = _verificationRequests.value.map { req ->
      if (req.id == requestId) req.copy(status = VerificationStatus.APPROVED, reviewNote = "Approved by CEO Bir Pujara") else req
    }
    syncUserBadges(targetReq.user.id, isBlueVerified = true)
    addAdminLog(adminUser.name, "Approved Verification", "Granted blue badge to ${targetReq.user.name} (@${targetReq.user.username})")
    return true
  }

  fun approveVerificationRequest(requestId: String): Boolean =
    approveVerificationRequest(requestId, ceoUser)

  // CEO Bir Pujara: Reject Verification Request
  fun rejectVerificationRequest(requestId: String, note: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false // Security check: Only CEO Bir Pujara
    val targetReq = _verificationRequests.value.find { it.id == requestId } ?: return false
    _verificationRequests.value = _verificationRequests.value.map { req ->
      if (req.id == requestId) req.copy(status = VerificationStatus.REJECTED, reviewNote = note.ifBlank { "Insufficient public presence documents" }) else req
    }
    addAdminLog(adminUser.name, "Rejected Verification", "Rejected application for ${targetReq.user.name}: $note")
    return true
  }

  fun rejectVerificationRequest(requestId: String, note: String): Boolean =
    rejectVerificationRequest(requestId, note, ceoUser)

  // CEO Bir Pujara: Grant Blue Badge directly
  fun grantBlueBadgeToUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false // Security check
    syncUserBadges(userId, isBlueVerified = true)
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Granted Blue Badge", "Directly granted blue badge to ${u?.name ?: userId}")
    return true
  }

  fun grantBlueBadge(userId: String): Boolean =
    grantBlueBadgeToUser(userId, ceoUser)

  fun removeBlueBadge(userId: String): Boolean =
    removeBlueBadgeFromUser(userId, ceoUser)

  fun promoteToModerator(userId: String): Boolean =
    promoteToModerator(userId, ceoUser)

  fun demoteModerator(userId: String): Boolean =
    demoteModerator(userId, ceoUser)

  fun blockUser(userId: String): Boolean =
    blockUser(userId, ceoUser)

  fun unblockUser(userId: String): Boolean =
    unblockUser(userId, ceoUser)

  fun suspendUser(userId: String): Boolean =
    suspendUser(userId, ceoUser)

  fun reactivateUser(userId: String): Boolean =
    reactivateUser(userId, ceoUser)

  fun reportPost(postId: String, reason: String) =
    reportPost(postId, reason, userAarav)

  fun removePost(postId: String): Boolean =
    removePost(postId, ceoUser)

  fun dismissReport(reportId: String): Boolean =
    dismissReport(reportId, ceoUser)

  fun createAnnouncement(title: String, body: String, nepaliTitle: String = "", nepaliMessage: String = ""): Boolean =
    createAnnouncement(title, nepaliTitle.ifBlank { title }, "", body, nepaliMessage.ifBlank { body }, "", ceoUser)


  // CEO Bir Pujara: Remove Blue Badge directly
  fun removeBlueBadgeFromUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false // Security check
    syncUserBadges(userId, isBlueVerified = false)
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Removed Blue Badge", "Removed blue badge from ${u?.name ?: userId}")
    return true
  }

  // Moderator Management (Only CEO can appoint or remove moderators)
  fun promoteToModerator(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(role = "moderator") else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Promoted Moderator", "Appointed ${u?.name ?: userId} as platform moderator")
    return true
  }

  fun demoteModerator(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(role = "user") else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Removed Moderator", "Demoted ${u?.name ?: userId} to standard member")
    return true
  }

  // User Account Controls: Block / Suspend / Reactivate
  fun blockUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(isBlocked = true) else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Blocked User", "Blocked account of ${u?.name ?: userId}")
    return true
  }

  fun unblockUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(isBlocked = false) else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Unblocked User", "Restored access for ${u?.name ?: userId}")
    return true
  }

  fun suspendUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(isSuspended = true) else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Suspended User", "Suspended account of ${u?.name ?: userId}")
    return true
  }

  fun reactivateUser(userId: String, adminUser: User): Boolean {
    if (!adminUser.isCeo) return false
    _allUsers.value = _allUsers.value.map { if (it.id == userId) it.copy(isSuspended = false) else it }
    val u = _allUsers.value.find { it.id == userId }
    addAdminLog(adminUser.name, "Reactivated User", "Reactivated account of ${u?.name ?: userId}")
    return true
  }

  // Content Moderation (CEO & Appointed Moderators)
  fun reportPost(postId: String, reason: String, reporter: User) {
    _posts.value = _posts.value.map { if (it.id == postId) it.copy(isReported = true, reportReason = reason) else it }
    val post = _posts.value.find { it.id == postId }
    val rep = Report(
      id = "rep_${System.currentTimeMillis()}",
      reporterUserId = reporter.id,
      reporterName = reporter.name,
      reportedType = "post",
      targetId = postId,
      targetContentSnippet = post?.content?.take(60) ?: "Reported post",
      reason = reason,
      status = ReportStatus.PENDING,
      createdAt = "Just now"
    )
    _reports.value = listOf(rep) + _reports.value
  }

  fun removePost(postId: String, staffUser: User): Boolean {
    if (!staffUser.isCeo && !staffUser.isModerator) return false
    _posts.value = _posts.value.map { if (it.id == postId) it.copy(isRemoved = true) else it }
    _reports.value = _reports.value.map { if (it.targetId == postId) it.copy(status = ReportStatus.RESOLVED) else it }
    addAdminLog(staffUser.name, "Removed Inappropriate Post", "Removed offending post ID: $postId")
    return true
  }

  fun dismissReport(reportId: String, staffUser: User): Boolean {
    if (!staffUser.isCeo && !staffUser.isModerator) return false
    val rep = _reports.value.find { it.id == reportId }
    _reports.value = _reports.value.map { if (it.id == reportId) it.copy(status = ReportStatus.DISMISSED) else it }
    if (rep != null) {
      _posts.value = _posts.value.map { if (it.id == rep.targetId) it.copy(isReported = false) else it }
    }
    return true
  }

  // Broadcast Platform Announcement
  fun createAnnouncement(
    title: String,
    nepaliTitle: String,
    hindiTitle: String = "",
    message: String,
    nepaliMessage: String,
    hindiMessage: String = "",
    adminUser: User
  ): Boolean {
    if (!adminUser.isCeo) return false
    val ann = Announcement(
      id = "ann_${System.currentTimeMillis()}",
      title = title,
      nepaliTitle = nepaliTitle,
      hindiTitle = hindiTitle,
      message = message,
      nepaliMessage = nepaliMessage,
      hindiMessage = hindiMessage,
      authorName = "Bir Pujara (CEO)",
      createdAt = "Just now"
    )
    _announcements.value = listOf(ann) + _announcements.value
    // Also create feed announcement post
    val announcementPost = Post(
      id = "post_ann_${System.currentTimeMillis()}",
      author = ceoUser,
      timestamp = "Just now",
      content = "$title\n\n$message",
      nepaliContent = "$nepaliTitle\n\n$nepaliMessage",
      hindiContent = "$hindiTitle\n\n$hindiMessage",
      mediaType = MediaType.TEXT_ONLY,
      tags = listOf("#OfficialAnnouncement", "#CEOUpdate", "#BhakariAlert"),
      isCeoAnnouncement = true,
      likesCount = 120,
      commentsCount = 14,
      sharesCount = 50,
      isLiked = true
    )
    _posts.value = listOf(announcementPost) + _posts.value
    addAdminLog(adminUser.name, "Broadcast Announcement", "Sent platform announcement: $title")
    return true
  }

  private fun addAdminLog(adminName: String, action: String, details: String) {
    val log = AdminLog(
      id = "log_${System.currentTimeMillis()}",
      adminName = adminName,
      action = action,
      details = details,
      timestamp = "Just now"
    )
    _adminLogs.value = listOf(log) + _adminLogs.value
  }

  private fun syncUserBadges(userId: String, isBlueVerified: Boolean) {
    _allUsers.value = _allUsers.value.map { u ->
      if (u.id == userId) u.copy(isBlueVerified = isBlueVerified, isVerified = isBlueVerified) else u
    }
    if (_currentUser.value?.id == userId) {
      _currentUser.value = _currentUser.value?.copy(isBlueVerified = isBlueVerified, isVerified = isBlueVerified)
    }
    _posts.value = _posts.value.map { post ->
      val updatedAuthor = if (post.author.id == userId) {
        post.author.copy(isBlueVerified = isBlueVerified, isVerified = isBlueVerified)
      } else post.author
      val updatedComments = post.comments.map { comment ->
        if (comment.author.id == userId) {
          comment.copy(author = comment.author.copy(isBlueVerified = isBlueVerified, isVerified = isBlueVerified))
        } else comment
      }
      post.copy(author = updatedAuthor, comments = updatedComments)
    }
  }

  fun searchUsers(query: String): List<User> {
    if (query.isBlank()) return _allUsers.value
    return _allUsers.value.filter {
      it.name.contains(query, ignoreCase = true) ||
      it.nepaliName.contains(query) ||
      it.username.contains(query, ignoreCase = true) ||
      it.title.contains(query, ignoreCase = true)
    }
  }

  // Auth Operations
  fun loginWithPhone(phone: String, otp: String): Boolean {
    val user = User(
      id = "user_phone_${System.currentTimeMillis()}",
      name = "Bhakari User",
      nepaliName = "भकारी प्रयोगकर्ता",
      hindiName = "भकारी उपयोगकर्ता",
      username = "user_${phone.takeLast(4)}",
      phone = phone,
      isVerified = false,
      isBlueVerified = false,
      role = "user"
    )
    _currentUser.value = user
    return true
  }

  fun loginWithEmail(email: String, pass: String): Boolean {
    val cleanEmail = if (email.isBlank()) "user@bhakari.np" else email
    val namePart = cleanEmail.substringBefore("@").replace(".", " ").capitalizeWords()
    
    // Check if logging in as CEO Bir Pujara
    if (cleanEmail.contains("birbahadurpujara", ignoreCase = true) || cleanEmail.contains("birpujara", ignoreCase = true)) {
      _currentUser.value = ceoUser
      return true
    }

    _currentUser.value = User(
      id = "user_email_${System.currentTimeMillis()}",
      name = namePart,
      nepaliName = namePart,
      hindiName = namePart,
      username = cleanEmail.substringBefore("@"),
      avatarColorHex = 0xFF8B5CF6,
      isVerified = false,
      isBlueVerified = false,
      role = "user",
      email = cleanEmail
    )
    return true
  }

  fun loginWithGoogle(email: String, displayName: String, avatarUrl: String? = null, idToken: String = ""): Boolean {
    if (email.contains("birbahadurpujara", ignoreCase = true) || email.contains("birpujara", ignoreCase = true)) {
      _currentUser.value = ceoUser
      return true
    }
    val googleUser = User(
      id = "user_google_${System.currentTimeMillis()}",
      name = displayName.ifBlank { "Google User" },
      nepaliName = displayName.ifBlank { "गुगल प्रयोगकर्ता" },
      hindiName = displayName.ifBlank { "गूगल उपयोगकर्ता" },
      username = email.substringBefore("@"),
      email = email,
      avatarUrl = avatarUrl ?: "",
      isVerified = false,
      isBlueVerified = false,
      role = "user"
    )
    _currentUser.value = googleUser
    updateUserProfile(googleUser)
    return true
  }

  fun loginWithGoogle(idToken: String, displayName: String, email: String): Boolean =
    loginWithGoogle(email = email, displayName = displayName, avatarUrl = null, idToken = idToken)

  fun signupWithEmail(fullName: String, email: String, pass: String): Boolean {
    val validName = if (fullName.isBlank()) "New Member" else fullName
    val validEmail = if (email.isBlank()) "member@bhakari.np" else email
    val newUser = User(
      id = "user_signup_${System.currentTimeMillis()}",
      name = validName,
      nepaliName = validName,
      hindiName = validName,
      username = validEmail.substringBefore("@"),
      avatarColorHex = 0xFFEC4899,
      isVerified = false,
      isBlueVerified = false,
      role = "user",
      email = validEmail
    )
    _currentUser.value = newUser
    updateUserProfile(newUser)
    return true
  }

  fun loginAsCeo() {
    _currentUser.value = ceoUser
  }

  fun logout() {
    _currentUser.value = null
  }

  private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
