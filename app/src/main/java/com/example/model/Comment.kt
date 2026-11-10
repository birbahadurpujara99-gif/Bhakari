package com.example.model

data class Comment(
  val id: String,
  val author: User,
  val text: String,
  val nepaliText: String = "",
  val timestamp: String,
  val likesCount: Int = 0
)
