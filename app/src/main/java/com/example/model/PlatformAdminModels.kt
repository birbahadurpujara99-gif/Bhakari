package com.example.model

import com.google.firebase.firestore.IgnoreExtraProperties

enum class ReportStatus {
  PENDING,
  RESOLVED,
  DISMISSED
}

@IgnoreExtraProperties
data class Report(
  val id: String = "",
  val reporterUserId: String = "",
  val reporterName: String = "",
  val reportedType: String = "post", // "post", "comment", "user"
  val targetId: String = "",
  val targetContentSnippet: String = "",
  val reason: String = "",
  val status: ReportStatus = ReportStatus.PENDING,
  val createdAt: String = "Just now",
  val resolvedBy: String = ""
)

@IgnoreExtraProperties
data class Announcement(
  val id: String = "",
  val title: String = "",
  val nepaliTitle: String = "",
  val hindiTitle: String = "",
  val message: String = "",
  val nepaliMessage: String = "",
  val hindiMessage: String = "",
  val authorName: String = "Bir Pujara (CEO)",
  val createdAt: String = "Just now"
)

@IgnoreExtraProperties
data class AdminLog(
  val id: String = "",
  val adminName: String = "Bir Pujara (CEO)",
  val action: String = "",
  val details: String = "",
  val timestamp: String = "Just now"
)
