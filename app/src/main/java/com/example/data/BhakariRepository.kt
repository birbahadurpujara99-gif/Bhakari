package com.example.data

import com.example.R
import com.example.model.BluetoothDevicePeer
import com.example.model.ChatMessage
import com.example.model.Comment
import com.example.model.Conversation
import com.example.model.MediaType
import com.example.model.Post
import com.example.model.TransferStatus
import com.example.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BhakariRepository {

  private val coroutineScope = CoroutineScope(Dispatchers.Main)

  // CEO Bir Pujara - Official Verified Account with Green Verified Tick
  val ceoUser = User(
    id = "ceo_bir_pujara",
    name = "Bir Pujara",
    nepaliName = "वीर पुजारा",
    username = "birpujara_ceo",
    avatarRes = R.drawable.ceo_bir_pujara,
    avatarColorHex = 0xFF10B981,
    isVerified = true,
    isCeo = true,
    title = "Founder & CEO, Bhakari",
    nepaliTitle = "संस्थापक तथा सिईओ, भकारी",
    bio = "CEO & Founder of Bhakari Social Media. Connecting Nepalis worldwide with our secure social platform & Bluetooth nearby sharing. गर्वका साथ नेपाली! 🇳🇵🌾",
    nepaliBio = "भकारी सामाजिक सञ्जालका संस्थापक तथा सिईओ। विश्वभरका नेपालीहरूलाई जोड्दै, सुरक्षित प्रविधि र ब्लुटुथ सेयरिङका साथ! गर्वका साथ नेपाली! 🇳🇵🌾",
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
    username = "aarav_nepal",
    avatarColorHex = 0xFF3B82F6,
    isVerified = true,
    title = "Traveler & Photographer",
    nepaliTitle = "यात्री तथा फोटोग्राफर",
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
    username = "prakriti_vibes",
    avatarColorHex = 0xFFEC4899,
    isVerified = false,
    title = "Cultural Creator",
    nepaliTitle = "सांस्कृतिक सिर्जनाकर्ता",
    bio = "Celebrating Nepali art, songs, and food! 🥟🪔",
    nepaliBio = "नेपाली कला, गीत र परिकारको उत्सव! 🥟🪔",
    followersCount = 8900,
    followingCount = 420,
    postsCount = 64
  )

  // Current Logged In User
  private val _currentUser = MutableStateFlow<User?>(
    User(
      id = "me_suman",
      name = "Suman Thapa",
      nepaliName = "सुमन थापा",
      username = "suman_thapa",
      avatarColorHex = 0xFFF59E0B,
      isVerified = false,
      title = "Bhakari Member",
      nepaliTitle = "भकारी सदस्य",
      bio = "Tech enthusiast from Kathmandu. Loving the new Bhakari app! 🚀",
      nepaliBio = "काठमाडौंबाट प्रविधि प्रेमी। नयाँ भकारी एप निकै मन पर्यो! 🚀",
      email = "suman.thapa@example.com",
      phone = "+977 9841000000",
      followersCount = 420,
      followingCount = 180,
      postsCount = 12
    )
  )
  val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

  // App Language State
  private val _currentLanguage = MutableStateFlow(AppLanguage.NEPALI)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  fun toggleLanguage() {
    _currentLanguage.value = if (_currentLanguage.value == AppLanguage.NEPALI) {
      AppLanguage.ENGLISH
    } else {
      AppLanguage.NEPALI
    }
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

  init {
    initSeedData()
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
        content = "Namaste everyone! 🌾 As CEO, I am thrilled to officially launch Bhakari - our very own indigenous social media platform. You can now post photos, videos, chat directly, and share posts & contacts offline with nearby friends using Bluetooth! Thank you for trusting us. Proudly Nepali! 🇳🇵✨",
        nepaliContent = "नमस्ते सम्पूर्णमा! 🌾 सिईओको रूपमा, नेपालको आफ्नै मौलिक रङ्गीन सामाजिक सञ्जाल भकारीको शुभारम्भ गर्न पाउँदा म अत्यन्तै हर्षित छु। अब फोटो, भिडियो पोस्ट गर्न, च्याट गर्न र इन्टरनेट बिना नै नजिकका साथीहरूसँग ब्लुटुथ मार्फत पोस्ट र सम्पर्क आदान-प्रदान गर्न सक्नुहुन्छ! सबैलाई हार्दिक स्वागत छ! 🇳🇵✨",
        mediaType = MediaType.PHOTO,
        imageRes = R.drawable.bhakari_cover_art,
        tags = listOf("#BhakariLaunch", "#CEOAnnouncement", "#BirPujara", "#DigitalNepal", "#ConnectingNepalis"),
        likesCount = 3842,
        commentsCount = 248,
        sharesCount = 590,
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
      ),
      Post(
        id = "post_ceo_tech",
        author = ceoUser,
        timestamp = "1 day ago",
        content = "Tip from CEO Bir Pujara: Did you know you can share your favorite Bhakari posts and contact card with nearby users using our Bluetooth Radar? Even without mobile data, connection never stops! 📡📱",
        nepaliContent = "सिईओ वीर पुजाराको जानकारी: के तपाईंलाई थाहा छ, तपाईंले आफ्नो मनपर्ने भकारी पोस्ट र सम्पर्क कार्ड ब्लुटुथ राडारबाट वरपरका साथीहरूलाई पठाउन सक्नुहुन्छ? इन्टरनेट डेटा सकिए पनि हाम्रो सम्पर्क टुट्दैन! 📡📱",
        mediaType = MediaType.TEXT_ONLY,
        tags = listOf("#BhakariFeatures", "#BluetoothShare", "#OfflineSocial", "#NepaliInnovation"),
        likesCount = 2150,
        commentsCount = 112,
        sharesCount = 330,
        isLiked = true,
        comments = emptyList(),
        isCeoAnnouncement = true,
        location = "Bhakari HQ, Nepal"
      )
    )

    _posts.value = seedPosts

    // Initial Chat Messages with CEO Bir Pujara
    val ceoChatHistory = listOf(
      ChatMessage(
        id = "m1",
        senderId = ceoUser.id,
        recipientId = "me_suman",
        text = "Namaste Suman! Welcome to Bhakari. As CEO, I am here to ensure you have a wonderful experience on our platform. 🇳🇵🌾",
        nepaliText = "नमस्ते सुमनजी! भकारीमा हार्दिक स्वागत छ। हाम्रो प्लेटफर्ममा तपाईंको अनुभव उत्कृष्ट बनाउन सिईओको रूपमा म सधैं तयार छु। 🇳🇵🌾",
        timestamp = "10:30 AM",
        isFromMe = false
      ),
      ChatMessage(
        id = "m2",
        senderId = "me_suman",
        recipientId = ceoUser.id,
        text = "Namaste CEO Bir Pujara Sir! Really loving the colorful design and the Bluetooth sharing feature. Great job!",
        nepaliText = "नमस्ते सिईओ वीर पुजारा सर! रङ्गीन डिजाइन र ब्लुटुथ सेयरिङ फिचर मलाई निकै मन पर्यो। धेरै धेरै धन्यवाद!",
        timestamp = "10:32 AM",
        isFromMe = true
      ),
      ChatMessage(
        id = "m3",
        senderId = ceoUser.id,
        recipientId = "me_suman",
        text = "Thank you so much! Feel free to suggest any new features directly here. Enjoy connecting!",
        nepaliText = "धेरै धेरै धन्यवाद! कुनै पनि नयाँ सुझाव भए मलाई सिधै यहीँ सन्देश पठाउन सक्नुहुन्छ। रमाउनुहोस्!",
        timestamp = "10:35 AM",
        isFromMe = false
      )
    )

    _chatMessages.value = mapOf(
      ceoUser.id to ceoChatHistory
    )

    // Initial Nearby Bluetooth Peers
    _nearbyPeers.value = listOf(
      BluetoothDevicePeer(
        id = "bt_ceo_device",
        name = "Bir Pujara's Flagship (CEO)",
        deviceAddress = "74:D2:1D:9C:B1:01",
        rssi = -42, // Strong signal
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
      ),
      BluetoothDevicePeer(
        id = "bt_kathmandu_hub",
        name = "Kathmandu Tech Hub Router",
        deviceAddress = "E0:2B:99:33:44:55",
        rssi = -75,
        isBhakariUser = false
      )
    )
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
      if (post.id == postId) {
        post.copy(sharesCount = post.sharesCount + 1)
      } else {
        post
      }
    }
  }

  // Create new Post
  fun createPost(content: String, mediaType: MediaType, tag: String = "#Bhakari") {
    val author = _currentUser.value ?: userAarav
    val newPost = Post(
      id = "post_${System.currentTimeMillis()}",
      author = author,
      timestamp = "Just now",
      content = content,
      nepaliContent = content,
      mediaType = mediaType,
      imageRes = if (mediaType == MediaType.PHOTO) R.drawable.bhakari_cover_art else null,
      videoThumbnailRes = if (mediaType == MediaType.VIDEO) R.drawable.bhakari_cover_art else null,
      videoTitle = if (mediaType == MediaType.VIDEO) "Bhakari Community Video" else null,
      tags = listOf(tag, "#CommunityPost", "#Bhakari"),
      likesCount = 1,
      commentsCount = 0,
      sharesCount = 0,
      isLiked = true,
      comments = emptyList(),
      location = "Nepal"
    )
    _posts.value = listOf(newPost) + _posts.value
  }

  // Send Chat Message
  fun sendMessage(recipientId: String, text: String) {
    val currentList = _chatMessages.value[recipientId] ?: emptyList()
    val msg = ChatMessage(
      id = "m_${System.currentTimeMillis()}",
      senderId = _currentUser.value?.id ?: "me_suman",
      recipientId = recipientId,
      text = text,
      nepaliText = text,
      timestamp = "Just now",
      isFromMe = true
    )
    _chatMessages.value = _chatMessages.value + (recipientId to (currentList + msg))

    // If talking to CEO Bir Pujara, simulate an immediate warm CEO response
    if (recipientId == ceoUser.id) {
      coroutineScope.launch {
        delay(1200)
        val repliesEn = listOf(
          "Thank you for reaching out, my friend! We are committed to making Bhakari the best social platform for all Nepalis. 🌾🇳🇵",
          "Noted! As CEO, I am personally reviewing feedback from our community. Thank you for your support!",
          "Great to hear from you! Don't forget to try our nearby Bluetooth post sharing feature with your friends."
        )
        val repliesNe = listOf(
          "सम्पर्क गर्नुभएकोमा धन्यवाद साथी! भकारीलाई सम्पूर्ण नेपालीहरूको उत्कृष्ट सामाजिक सञ्जाल बनाउन हामी समर्पित छौं। 🌾🇳🇵",
          "मैले टिपेको छु! सिईओको रूपमा समुदायबाट आउने हरेक सुझावलाई म व्यक्तिगत रूपमा हेरिरहेको छु।",
          "तपाईंसँग कुरा गर्न पाउँदा खुसी लाग्यो! आफ्ना साथीहरूसँग ब्लुटुथ नजिकैको पोस्ट सेयरिङ फिचर पनि प्रयोग गर्नुहोला।"
        )
        val randomIndex = (0..2).random()
        val ceoReply = ChatMessage(
          id = "m_reply_${System.currentTimeMillis()}",
          senderId = ceoUser.id,
          recipientId = _currentUser.value?.id ?: "me_suman",
          text = repliesEn[randomIndex],
          nepaliText = repliesNe[randomIndex],
          timestamp = "Just now",
          isFromMe = false
        )
        val updatedList = _chatMessages.value[recipientId] ?: emptyList()
        _chatMessages.value = _chatMessages.value + (recipientId to (updatedList + ceoReply))
      }
    }
  }

  // Bluetooth scanning simulation
  fun startBluetoothScan() {
    _isBluetoothScanning.value = true
    coroutineScope.launch {
      delay(2000)
      _isBluetoothScanning.value = false
    }
  }

  fun toggleDiscoverable() {
    _isDiscoverable.value = !_isDiscoverable.value
  }

  // Share Post over Bluetooth
  fun sharePostOverBluetooth(peerId: String, post: Post, onDone: (Boolean) -> Unit) {
    _nearbyPeers.value = _nearbyPeers.value.map { peer ->
      if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.SENDING) else peer
    }
    coroutineScope.launch {
      delay(1500)
      _nearbyPeers.value = _nearbyPeers.value.map { peer ->
        if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.COMPLETED) else peer
      }
      _lastTransferNotice.value = "Post '${post.content.take(25)}...' shared via Bluetooth!"
      onDone(true)
      delay(3000)
      _nearbyPeers.value = _nearbyPeers.value.map { peer ->
        if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.IDLE) else peer
      }
    }
  }

  // Share Contact over Bluetooth
  fun shareContactOverBluetooth(peerId: String, onDone: (Boolean) -> Unit) {
    val me = _currentUser.value ?: userAarav
    _nearbyPeers.value = _nearbyPeers.value.map { peer ->
      if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.SENDING) else peer
    }
    coroutineScope.launch {
      delay(1500)
      _nearbyPeers.value = _nearbyPeers.value.map { peer ->
        if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.COMPLETED) else peer
      }
      _lastTransferNotice.value = "Contact Card of ${me.name} (${me.phone}) sent via Bluetooth!"
      onDone(true)
      delay(3000)
      _nearbyPeers.value = _nearbyPeers.value.map { peer ->
        if (peer.id == peerId) peer.copy(transferStatus = TransferStatus.IDLE) else peer
      }
    }
  }

  // Authentication
  fun loginWithPhone(phoneNumber: String, code: String): Boolean {
    val cleanPhone = if (phoneNumber.isBlank()) "+977 9800000000" else phoneNumber
    _currentUser.value = User(
      id = "user_phone_${System.currentTimeMillis()}",
      name = "Phone User (${cleanPhone.takeLast(4)})",
      nepaliName = "फोन प्रयोगकर्ता (${cleanPhone.takeLast(4)})",
      username = "user_${cleanPhone.filter { it.isDigit() }.takeLast(6)}",
      avatarColorHex = 0xFF10B981,
      isVerified = false,
      title = "Bhakari Phone Member",
      nepaliTitle = "भकारी फोन सदस्य",
      bio = "Connected via phone number authentication. 🇳🇵",
      nepaliBio = "मोबाइल नम्बर मार्फत जोडिनुभएको सदस्य। 🇳🇵",
      phone = cleanPhone,
      email = "user@bhakari.np"
    )
    return true
  }

  fun loginWithEmail(email: String, pass: String): Boolean {
    val cleanEmail = if (email.isBlank()) "user@bhakari.np" else email
    val namePart = cleanEmail.substringBefore("@").replace(".", " ").capitalizeWords()
    
    // Check if logging in as CEO
    if (cleanEmail.contains("birbahadurpujara", ignoreCase = true) || cleanEmail.contains("birpujara", ignoreCase = true)) {
      _currentUser.value = ceoUser
      return true
    }

    _currentUser.value = User(
      id = "user_email_${System.currentTimeMillis()}",
      name = namePart,
      nepaliName = namePart,
      username = cleanEmail.substringBefore("@"),
      avatarColorHex = 0xFF8B5CF6,
      isVerified = false,
      title = "Bhakari Member",
      nepaliTitle = "भकारी सदस्य",
      bio = "Active Bhakari community member. 🌾",
      nepaliBio = "भकारी समुदायका सक्रिय सदस्य। 🌾",
      email = cleanEmail,
      phone = "+977 98XXXXXXXX"
    )
    return true
  }

  fun signupWithEmail(fullName: String, email: String, pass: String): Boolean {
    val validName = if (fullName.isBlank()) "New Member" else fullName
    val validEmail = if (email.isBlank()) "member@bhakari.np" else email
    _currentUser.value = User(
      id = "user_signup_${System.currentTimeMillis()}",
      name = validName,
      nepaliName = validName,
      username = validEmail.substringBefore("@"),
      avatarColorHex = 0xFFEC4899,
      isVerified = false,
      title = "New Bhakari Creator",
      nepaliTitle = "नयाँ भकारी सिर्जनाकर्ता",
      bio = "Excited to share stories and connect on Bhakari! 🇳🇵",
      nepaliBio = "भकारीमा जोडिएर कथा र विचारहरू बाँड्न पाउँदा उत्साहित! 🇳🇵",
      email = validEmail,
      phone = "+977 98XXXXXXXX"
    )
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
