package com.example.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
  val id: String = "",
  val name: String = "",
  val nepaliName: String = "",
  val hindiName: String = "",
  val username: String = "",
  val avatarRes: Int? = null,
  val avatarUrl: String = "",
  val coverUrl: String = "",
  val avatarColorHex: Long = 0xFF3B82F6,
  val isVerified: Boolean = false,
  val isBlueVerified: Boolean = false,
  val isCeo: Boolean = false,
  val role: String = "user", // "ceo", "moderator", "user"
  val title: String = "",
  val nepaliTitle: String = "",
  val hindiTitle: String = "",
  val bio: String = "",
  val nepaliBio: String = "",
  val hindiBio: String = "",
  val email: String = "",
  val phone: String = "",
  val followersCount: Int = 0,
  val followingCount: Int = 0,
  val postsCount: Int = 0,
  val isFollowedByMe: Boolean = false,
  val isBlocked: Boolean = false,
  val isSuspended: Boolean = false,
  val isPrivateAccount: Boolean = false,
  val allowMessagesFromAnyone: Boolean = true,
  val allowBluetoothDiscovery: Boolean = true
) {
  // CEO has permanent blue verified badge as well as any approved blue verified user
  val hasBlueBadge: Boolean get() = isCeo || isBlueVerified || isVerified
  val isModerator: Boolean get() = role == "moderator" || role == "admin"
  val isAdministrator: Boolean get() = isCeo || role == "ceo"
}
