package com.example.model

data class ChatMessage(
  val id: String,
  val senderId: String,
  val recipientId: String,
  val text: String,
  val nepaliText: String = "",
  val timestamp: String,
  val isFromMe: Boolean,
  val isDelivered: Boolean = true,
  val isRead: Boolean = true,
  val sharedPostId: String? = null,
  val isBluetoothDelivered: Boolean = false
)

data class Conversation(
  val peer: User,
  val lastMessage: ChatMessage,
  val unreadCount: Int = 0
)
