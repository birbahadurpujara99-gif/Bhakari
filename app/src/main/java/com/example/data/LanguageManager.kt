package com.example.data

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
  ENGLISH("en", "English", "🇬🇧"),
  NEPALI("ne", "नेपाली", "🇳🇵")
}

object LanguageStrings {
  private val en = mapOf(
    "app_name" to "Bhakari",
    "app_tagline" to "Connecting Nepalis Worldwide",
    "switch_lang" to "नेपाली",
    "nav_home" to "Home",
    "nav_explore" to "Explore",
    "nav_create" to "Post",
    "nav_messages" to "Messages",
    "nav_bluetooth" to "Nearby",
    "nav_profile" to "Profile",
    
    // Auth
    "login_title" to "Welcome to Bhakari",
    "login_subtitle" to "The vibrant Nepali social media platform",
    "tab_phone" to "Phone Number",
    "tab_email" to "Email Account",
    "tab_signup" to "Sign Up",
    "phone_label" to "Phone Number",
    "phone_placeholder" to "+977 98XXXXXXXX",
    "email_label" to "Email Address",
    "email_placeholder" to "name@example.com",
    "password_label" to "Password",
    "password_placeholder" to "Enter your password",
    "fullname_label" to "Full Name",
    "fullname_placeholder" to "Enter your full name",
    "login_btn" to "Sign In",
    "signup_btn" to "Create Account",
    "guest_btn" to "Continue as Guest",
    "or_divider" to "OR",
    "phone_login_desc" to "Instant login with phone number verification",
    "email_login_desc" to "Secure login with registered email and password",
    "signup_desc" to "Join the Bhakari community today",
    "logout" to "Log Out",
    "logged_in_as" to "Logged in as",

    // Home / Feed
    "stories_title" to "Stories & Moments",
    "whats_on_mind" to "What's on your mind? Share with Bhakari...",
    "btn_photo" to "Photo",
    "btn_video" to "Video",
    "btn_text" to "Text",
    "filter_all" to "All",
    "filter_trending" to "Trending",
    "filter_ceo" to "CEO Updates",
    "filter_nepal" to "Nepal Culture",
    "filter_nearby" to "Nearby Bluetooth",
    "like" to "Like",
    "liked" to "Liked",
    "comment" to "Comment",
    "comments" to "Comments",
    "share" to "Share",
    "write_comment" to "Write a warm comment...",
    "post_comment" to "Post",
    "verified_ceo" to "CEO • Verified",
    "verified_user" to "Verified",
    "official_announcement" to "Official CEO Announcement",

    // Post Creation
    "create_post_title" to "Create Post",
    "select_media_type" to "Select Media Type",
    "caption_placeholder" to "Write something meaningful for the community...",
    "publish_post" to "Publish to Bhakari",
    "post_published" to "Post published successfully!",

    // Messages
    "messages_title" to "Messages & Chats",
    "search_chats" to "Search chats...",
    "chat_with_ceo" to "Chat with CEO Bir Pujara",
    "ceo_available" to "Official CEO Direct Support Line",
    "type_message" to "Type a message...",
    "send" to "Send",

    // Bluetooth Nearby Share
    "bluetooth_title" to "Nearby Bluetooth Share",
    "bluetooth_subtitle" to "Share posts & contacts device-to-device offline",
    "bluetooth_scanning" to "Radar scanning for nearby Bhakari users...",
    "bluetooth_radar_active" to "Bluetooth Radar Active",
    "broadcast_toggle" to "Make My Device Discoverable",
    "devices_found" to "Nearby Users Found",
    "connect_device" to "Connect",
    "share_post_btn" to "Share Post via Bluetooth",
    "share_contact_btn" to "Share My Contact Card",
    "bluetooth_sent_success" to "Sent successfully via Bluetooth!",
    "bluetooth_received" to "Received new item via Bluetooth",
    "transfer_in_progress" to "Transferring over Bluetooth...",
    "mesh_mode" to "Peer-to-Peer Offline Sync",

    // CEO Profile
    "ceo_full_name" to "Bir Pujara",
    "ceo_role" to "Founder & CEO, Bhakari",
    "ceo_verified_badge" to "Verified CEO (Official)",
    "ceo_bio" to "Building Nepal's largest indigenous social media ecosystem. Empowering local creators, stories, and offline connectivity. Connecting Nepalis from Mechi to Mahakali and across the globe! 🇳🇵🌾",
    "followers" to "Followers",
    "following" to "Following",
    "posts" to "Posts",
    "follow" to "Follow",
    "following_active" to "Following",
    "message_ceo" to "Message CEO",
    "ceo_posts_heading" to "CEO Updates & Announcements",

    // PWA & Install
    "install_app_btn" to "Install App",
    "pwa_banner_title" to "Install Bhakari on your device",
    "pwa_banner_subtitle" to "Installable directly from Google Chrome as a PWA",
    "pwa_dialog_title" to "Install Bhakari App",
    "pwa_feature_offline" to "Offline Bluetooth Sharing & Local Feed",
    "pwa_feature_fast" to "Fast, lightweight & instant launch",
    "pwa_feature_home" to "Add directly to your Android Home Screen",
    "pwa_chrome_step" to "Chrome Menu: Tap (⋮) -> 'Install app' or 'Add to Home screen'",
    "pwa_install_now" to "Add Shortcut / Install",
    "pwa_installed_toast" to "Bhakari installed to Home Screen!",
    "close" to "Close"
  )

  private val ne = mapOf(
    "app_name" to "भकारी",
    "app_tagline" to "विश्वभरका नेपालीहरूलाई जोड्ने सामाजिक सञ्जाल",
    "switch_lang" to "English",
    "nav_home" to "गृहपृष्ठ",
    "nav_explore" to "अन्वेषण",
    "nav_create" to "नयाँ पोस्ट",
    "nav_messages" to "कुराकानी",
    "nav_bluetooth" to "नजिकै ब्लुटुथ",
    "nav_profile" to "प्रोफाइल",
    
    // Auth
    "login_title" to "भकारीमा स्वागत छ",
    "login_subtitle" to "नेपालीहरूको आफ्नै रङ्गीन सामाजिक सञ्जाल",
    "tab_phone" to "फोन नम्बर",
    "tab_email" to "इमेल खाता",
    "tab_signup" to "नयाँ खाता",
    "phone_label" to "मोबाइल नम्बर",
    "phone_placeholder" to "+९७७ ९८XXXXXXXX",
    "email_label" to "इमेल ठेगाना",
    "email_placeholder" to "name@example.com",
    "password_label" to "पासवर्ड",
    "password_placeholder" to "आफ्नो पासवर्ड प्रविष्ट गर्नुहोस्",
    "fullname_label" to "पूरा नाम",
    "fullname_placeholder" to "तपाईंको पूरा नाम लेख्नुहोस्",
    "login_btn" to "लगइन गर्नुहोस्",
    "signup_btn" to "खाता सिर्जना गर्नुहोस्",
    "guest_btn" to "अतिथिको रूपमा अगाडि बढ्नुहोस्",
    "or_divider" to "वा",
    "phone_login_desc" to "फोन नम्बरबाट सहज र छिटो लगइन",
    "email_login_desc" to "इमेल र पासवर्ड मार्फत सुरक्षित लगइन",
    "signup_desc" to "आजै भकारी परिवारमा जोडिनुहोस्",
    "logout" to "लगआउट",
    "logged_in_as" to "लगइन भएको प्रयोगकर्ता",

    // Home / Feed
    "stories_title" to "कथा र क्षणहरू",
    "whats_on_mind" to "तपाईंको मनमा के छ? भकारीमा बाँड्नुहोस्...",
    "btn_photo" to "तस्बिर",
    "btn_video" to "भिडियो",
    "btn_text" to "शब्दहरू",
    "filter_all" to "सबै",
    "filter_trending" to "ट्रेन्डिङ",
    "filter_ceo" to "सिईओ अपडेट",
    "filter_nepal" to "नेपाली संस्कृति",
    "filter_nearby" to "नजिकैको ब्लुटुथ",
    "like" to "मन पर्यो",
    "liked" to "मन पराइयो",
    "comment" to "प्रतिक्रिया",
    "comments" to "प्रतिक्रियाहरू",
    "share" to "साझेदारी",
    "write_comment" to "आफ्नो मायालु प्रतिक्रिया लेख्नुहोस्...",
    "post_comment" to "पठाउनुहोस्",
    "verified_ceo" to "सिईओ • प्रमाणित",
    "verified_user" to "प्रमाणित",
    "official_announcement" to "आधिकारिक सिईओ घोषणा",

    // Post Creation
    "create_post_title" to "नयाँ पोस्ट सिर्जना",
    "select_media_type" to "माध्यम छान्नुहोस्",
    "caption_placeholder" to "समुदायका लागि केही मीठो कुरा लेख्नुहोस्...",
    "publish_post" to "भकारीमा पोस्ट गर्नुहोस्",
    "post_published" to "पोस्ट सफलतापूर्वक प्रकाशित भयो!",

    // Messages
    "messages_title" to "सन्देश तथा कुराकानी",
    "search_chats" to "कुराकानी खोज्नुहोस्...",
    "chat_with_ceo" to "सिईओ वीर पुजारासँग कुराकानी",
    "ceo_available" to "आधिकारिक सिईओ प्रत्यक्ष सहायता लाइन",
    "type_message" to "सन्देश लेख्नुहोस्...",
    "send" to "पठाउनुहोस्",

    // Bluetooth Nearby Share
    "bluetooth_title" to "नजिकै ब्लुटुथ साझेदारी",
    "bluetooth_subtitle" to "इन्टरनेट बिना नै सिधै पोस्ट र सम्पर्क आदान-प्रदान",
    "bluetooth_scanning" to "वरपरका भकारी प्रयोगकर्ताहरू खोजिँदैछ...",
    "bluetooth_radar_active" to "ब्लुटुथ राडार सक्रिय छ",
    "broadcast_toggle" to "मेरो उपकरण अरूलाई देखाउनुहोस्",
    "devices_found" to "भेटिएका नजिकका प्रयोगकर्ताहरू",
    "connect_device" to "जोड्नुहोस्",
    "share_post_btn" to "ब्लुटुथबाट पोस्ट पठाउनुहोस्",
    "share_contact_btn" to "मेरो सम्पर्क कार्ड पठाउनुहोस्",
    "bluetooth_sent_success" to "ब्लुटुथ मार्फत सफलतापूर्वक पठाइयो!",
    "bluetooth_received" to "ब्लुटुथबाट नयाँ सामग्री प्राप्त भयो",
    "transfer_in_progress" to "ब्लुटुथ ट्रान्सफर हुँदैछ...",
    "mesh_mode" to "अफलाइन डिभाइस टु डिभाइस सिङ्क",

    // CEO Profile
    "ceo_full_name" to "वीर पुजारा",
    "ceo_role" to "संस्थापक तथा सिईओ, भकारी",
    "ceo_verified_badge" to "प्रमाणित सिईओ (आधिकारिक)",
    "ceo_bio" to "नेपालको आफ्नै मौलिक सामाजिक सञ्जाल निर्माण गर्दै। स्थानीय सिर्जना, मौलिक कथा र डिजिटल प्रविधिलाई सशक्त बनाउँदै। मेचीदेखि महाकालीसम्म र विश्वभर छरिएका नेपालीहरूलाई जोड्दै! 🇳🇵🌾",
    "followers" to "फलोअर्स",
    "following" to "फलो गर्दै",
    "posts" to "पोस्टहरू",
    "follow" to "फलो गर्नुहोस्",
    "following_active" to "फलो गरियो",
    "message_ceo" to "सिईओलाई सन्देश",
    "ceo_posts_heading" to "सिईओका अपडेट तथा घोषणाहरू",

    // PWA & Install
    "install_app_btn" to "एप इन्स्टल",
    "pwa_banner_title" to "आफ्नो मोबाइलमा भकारी इन्स्टल गर्नुहोस्",
    "pwa_banner_subtitle" to "गुगल क्रोम (Chrome) बाट सिधै PWA एपको रूपमा इन्स्टल गर्न मिल्ने",
    "pwa_dialog_title" to "भकारी एप स्थापना गर्नुहोस्",
    "pwa_feature_offline" to "अफलाइन ब्लुटुथ सेयरिङ र स्थानीय फिड",
    "pwa_feature_fast" to "छरितो, हलुका र तुरुन्त खुल्ने",
    "pwa_feature_home" to "सिधै एन्ड्रोइड होम स्क्रिनमा थप्न सकिने",
    "pwa_chrome_step" to "क्रोम मेनु: (⋮) थिच्नुहोस् -> 'Install app' वा 'Add to Home screen'",
    "pwa_install_now" to "सर्टकट थप्नुहोस् / इन्स्टल गर्नुहोस्",
    "pwa_installed_toast" to "भकारी होम स्क्रिनमा थपियो!",
    "close" to "बन्द गर्नुहोस्"
  )

  fun get(key: String, lang: AppLanguage): String {
    return when (lang) {
      AppLanguage.NEPALI -> ne[key] ?: en[key] ?: key
      AppLanguage.ENGLISH -> en[key] ?: key
    }
  }
}
