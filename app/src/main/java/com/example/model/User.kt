package com.example.model

data class User(
  val id: String,
  val name: String,
  val nepaliName: String,
  val username: String,
  val avatarRes: Int? = null,
  val avatarColorHex: Long = 0xFF3B82F6,
  val isVerified: Boolean = false,
  val isCeo: Boolean = false,
  val title: String = "",
  val nepaliTitle: String = "",
  val bio: String = "",
  val nepaliBio: String = "",
  val email: String = "",
  val phone: String = "",
  val followersCount: Int = 0,
  val followingCount: Int = 0,
  val postsCount: Int = 0,
  val isFollowedByMe: Boolean = false
)
