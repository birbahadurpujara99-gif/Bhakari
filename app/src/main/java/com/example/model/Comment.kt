package com.example.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
  val id: String = "",
  val author: User = User(),
  val text: String = "",
  val nepaliText: String = "",
  val hindiText: String = "",
  val timestamp: String = "Just now",
  val likesCount: Int = 0,
  val isReported: Boolean = false,
  val isRemoved: Boolean = false
)
