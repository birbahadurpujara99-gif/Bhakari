package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppLanguage
import com.example.data.BhakariRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Bhakari", appName)
  }

  @Test
  fun `verify CEO Bir Pujara has verified green tick and official status`() {
    val repository = BhakariRepository()
    val ceo = repository.ceoUser
    assertEquals("Bir Pujara", ceo.name)
    assertEquals("वीर पुजारा", ceo.nepaliName)
    assertTrue("CEO must have isVerified = true", ceo.isVerified)
    assertTrue("CEO must have isCeo = true", ceo.isCeo)
    assertEquals(0xFF10B981, ceo.avatarColorHex)
  }

  @Test
  fun `verify language toggle between Nepali and English`() {
    val repository = BhakariRepository()
    assertEquals(AppLanguage.NEPALI, repository.currentLanguage.value)
    repository.toggleLanguage()
    assertEquals(AppLanguage.ENGLISH, repository.currentLanguage.value)
    repository.toggleLanguage()
    assertEquals(AppLanguage.NEPALI, repository.currentLanguage.value)
  }
}
