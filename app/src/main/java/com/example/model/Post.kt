package com.example.model

import com.google.firebase.firestore.IgnoreExtraProperties

enum class MediaType {
  TEXT_ONLY,
  PHOTO,
  VIDEO
}

@IgnoreExtraProperties
data class Post(
  val id: String = "",
  val author: User = User(),
  val timestamp: String = "Just now",
  val content: String = "",
  val nepaliContent: String = "",
  val hindiContent: String = "",
  val mediaType: MediaType = MediaType.TEXT_ONLY,
  val imageRes: Int? = null,
  val imageUrl: String = "",
  val videoThumbnailRes: Int? = null,
  val videoUrl: String = "",
  val videoTitle: String? = null,
  val tags: List<String> = emptyList(),
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val sharesCount: Int = 0,
  val isLiked: Boolean = false,
  val comments: List<Comment> = emptyList(),
  val isCeoAnnouncement: Boolean = false,
  val isReported: Boolean = false,
  val isRemoved: Boolean = false,
  val reportReason: String = "",
  val location: String = "Nepal"
)
