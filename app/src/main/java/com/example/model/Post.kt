package com.example.model

enum class MediaType {
  TEXT_ONLY,
  PHOTO,
  VIDEO
}

data class Post(
  val id: String,
  val author: User,
  val timestamp: String,
  val content: String,
  val nepaliContent: String = "",
  val mediaType: MediaType = MediaType.TEXT_ONLY,
  val imageRes: Int? = null,
  val videoThumbnailRes: Int? = null,
  val videoTitle: String? = null,
  val tags: List<String> = emptyList(),
  val likesCount: Int = 0,
  val commentsCount: Int = 0,
  val sharesCount: Int = 0,
  val isLiked: Boolean = false,
  val comments: List<Comment> = emptyList(),
  val isCeoAnnouncement: Boolean = false,
  val location: String = "Nepal"
)
