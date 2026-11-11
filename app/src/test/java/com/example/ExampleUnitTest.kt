package com.example

import com.example.data.AppLanguage
import com.example.data.BhakariRepository
import com.example.model.VerificationStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun `verify CEO Bir Pujara has permanent blue verification and admin permissions`() {
    val repository = BhakariRepository()
    val ceo = repository.ceoUser

    assertEquals("Bir Pujara", ceo.name)
    assertEquals("वीर पुजारा", ceo.nepaliName)
    assertEquals("बीर पुजारा", ceo.hindiName)
    assertEquals("birpujara_ceo", ceo.username)
    assertTrue("CEO must have isCeo = true", ceo.isCeo)
    assertTrue("CEO must have isVerified = true", ceo.isVerified)
    assertTrue("CEO must have isBlueVerified = true", ceo.isBlueVerified)
    assertTrue("CEO must have hasBlueBadge = true", ceo.hasBlueBadge)
    assertEquals("ceo", ceo.role)
    assertEquals("Founder & CEO, Bhakari", ceo.title)
  }

  @Test
  fun `verify trilingual language cycling between Nepali English and Hindi`() {
    val repository = BhakariRepository()
    assertEquals(AppLanguage.NEPALI, repository.currentLanguage.value)
    
    repository.toggleLanguage()
    assertEquals(AppLanguage.HINDI, repository.currentLanguage.value)
    
    repository.toggleLanguage()
    assertEquals(AppLanguage.ENGLISH, repository.currentLanguage.value)
    
    repository.toggleLanguage()
    assertEquals(AppLanguage.NEPALI, repository.currentLanguage.value)
  }

  @Test
  fun `verify verification request submission and CEO approval flow`() {
    val repository = BhakariRepository()
    // Normal user logged in
    val initialUser = repository.currentUser.value
    assertNotNull(initialUser)
    assertFalse(initialUser!!.hasBlueBadge)

    // User submits request
    val submitted = repository.submitVerificationRequest("I am an authentic creator", "https://instagram.com/creator", "Creator")
    assertTrue(submitted)

    val requests = repository.verificationRequests.value
    assertTrue(requests.isNotEmpty())
    val myRequest = requests.first()
    assertEquals(VerificationStatus.PENDING, myRequest.status)

    // CEO approves request
    val approved = repository.approveVerificationRequest(myRequest.id)
    assertTrue(approved)

    val updatedRequest = repository.verificationRequests.value.find { it.id == myRequest.id }
    assertNotNull(updatedRequest)
    assertEquals(VerificationStatus.APPROVED, updatedRequest!!.status)

    // Verified badge now granted to user
    val updatedUser = repository.allUsers.value.find { it.id == initialUser.id }
    assertNotNull(updatedUser)
    assertTrue(updatedUser!!.hasBlueBadge)
  }

  @Test
  fun `verify CEO can grant and remove blue verification badge directly`() {
    val repository = BhakariRepository()
    val testUserId = "user_prakriti"

    repository.grantBlueBadge(testUserId)
    val userWithBadge = repository.allUsers.value.find { it.id == testUserId }
    assertNotNull(userWithBadge)
    assertTrue(userWithBadge!!.hasBlueBadge)

    repository.removeBlueBadge(testUserId)
    val userWithoutBadge = repository.allUsers.value.find { it.id == testUserId }
    assertNotNull(userWithoutBadge)
    assertFalse(userWithoutBadge!!.isBlueVerified)
  }

  @Test
  fun `verify CEO moderator promotion and removal`() {
    val repository = BhakariRepository()
    val testUserId = "user_prakriti"

    repository.promoteToModerator(testUserId)
    val mod = repository.allUsers.value.find { it.id == testUserId }
    assertNotNull(mod)
    assertEquals("moderator", mod!!.role)
    assertTrue(mod.isModerator)

    repository.demoteModerator(testUserId)
    val regular = repository.allUsers.value.find { it.id == testUserId }
    assertNotNull(regular)
    assertEquals("user", regular!!.role)
    assertFalse(regular.isModerator)
  }

  @Test
  fun `verify post reporting and moderation dismissal`() {
    val repository = BhakariRepository()
    val post = repository.posts.value.first()

    repository.reportPost(post.id, "Violates guidelines")
    val reportedPost = repository.posts.value.find { it.id == post.id }
    assertNotNull(reportedPost)
    assertTrue(reportedPost!!.isReported)

    val reports = repository.reports.value
    assertTrue(reports.any { it.targetId == post.id })

    val reportId = reports.first { it.targetId == post.id }.id
    repository.dismissReport(reportId)
    val dismissedPost = repository.posts.value.find { it.id == post.id }
    assertNotNull(dismissedPost)
    assertFalse(dismissedPost!!.isReported)
  }

  @Test
  fun `verify platform announcement broadcast`() {
    val repository = BhakariRepository()
    val title = "Server Maintenance Update"
    val body = "Scheduled upgrade on Saturday midnight"

    repository.createAnnouncement(title, body, "सर्भर मर्मत अपडेट", "शनिबार राति अपडेट हुनेछ")
    
    val announcements = repository.announcements.value
    assertTrue(announcements.any { it.title == title })

    val posts = repository.posts.value
    assertTrue(posts.any { it.isCeoAnnouncement && it.content.contains(title) })
  }
}
