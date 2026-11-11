package com.example.model

import com.google.firebase.firestore.IgnoreExtraProperties

enum class VerificationStatus {
  PENDING,
  APPROVED,
  REJECTED
}

@IgnoreExtraProperties
data class VerificationRequest(
  val id: String = "",
  val user: User = User(),
  val category: String = "",
  val nepaliCategory: String = "",
  val hindiCategory: String = "",
  val documentType: String = "",
  val reason: String = "",
  val knownAs: String = "",
  val websiteOrLink: String = "",
  val status: VerificationStatus = VerificationStatus.PENDING,
  val submittedAt: String = "Just now",
  val reviewNote: String = "",
  val reviewedBy: String = ""
)
