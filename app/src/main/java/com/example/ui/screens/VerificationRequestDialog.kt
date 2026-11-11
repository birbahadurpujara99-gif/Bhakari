package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.model.User
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.FacebookVerifiedBlue
import com.example.ui.theme.VerifiedGreen

@Composable
fun VerificationRequestDialog(
  currentUser: User,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onSubmit: (category: String, docType: String, reason: String, knownAs: String, link: String) -> Unit
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI

  val categories = listOf(
    if (isNepali) "सांस्कृतिक सिर्जनाकर्ता / कलाकार" else "Cultural Creator / Artist",
    if (isNepali) "पत्रकार / मिडिया संस्था" else "Journalist / News Media",
    if (isNepali) "सार्वजनिक व्यक्तित्व / उद्यमी" else "Public Figure / Entrepreneur",
    if (isNepali) "व्यवसाय तथा ब्राण्ड" else "Business / Community Organization",
    if (isNepali) "सामुदायिक अगुवा / अभियन्ता" else "Community Leader / Activist"
  )

  val docTypes = listOf(
    if (isNepali) "राष्ट्रिय परिचयपत्र (National ID)" else "National Identity Card",
    if (isNepali) "नागरिकता प्रमाणपत्र (Citizenship)" else "Nepali Citizenship Certificate",
    if (isNepali) "प्रेस काउन्सिल कार्ड (Press Card)" else "Press Council Nepal Card",
    if (isNepali) "राहदानी (Passport)" else "Valid Passport",
    if (isNepali) "कम्पनी दर्ता प्रमाणपत्र" else "Official Company Registration"
  )

  var selectedCategory by remember { mutableStateOf(categories[0]) }
  var categoryExpanded by remember { mutableStateOf(false) }

  var selectedDocType by remember { mutableStateOf(docTypes[0]) }
  var docExpanded by remember { mutableStateOf(false) }

  var knownAsText by remember { mutableStateOf(if (isNepali) currentUser.nepaliName else currentUser.name) }
  var reasonText by remember { mutableStateOf("") }
  var linkText by remember { mutableStateOf("") }
  var isSubmitting by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      shape = RoundedCornerShape(26.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .testTag("verification_request_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(FacebookVerifiedBlue.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              VerifiedBadge(size = 22.dp, isCeo = false)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = LanguageStrings.get("verification_title", currentLanguage),
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              Text(
                text = "Facebook-Style Blue Badge (🔵✓)",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = FacebookVerifiedBlue,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_verification_dialog_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Badge Preview Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = FacebookVerifiedBlue.copy(alpha = 0.08f)
          ),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(FacebookVerifiedBlue.copy(alpha = 0.35f))
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(currentUser.avatarColorHex)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentUser.name.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (isNepali) currentUser.nepaliName else currentUser.name,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                  )
                )
                Spacer(modifier = Modifier.width(5.dp))
                // Preview of the classic circular blue badge with white checkmark
                VerifiedBadge(size = 17.dp, isCeo = false)
              }
              Text(
                text = "@${currentUser.username} • ${if (isNepali) "प्रमाणित प्रोफाइल पूर्वावलोकन" else "Verified Profile Preview"}",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = FacebookVerifiedBlue,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = LanguageStrings.get("verification_desc", currentLanguage),
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Picker
        Text(
          text = LanguageStrings.get("category_label", currentLanguage),
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedButton(
            onClick = { categoryExpanded = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("select_category_btn")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = selectedCategory, maxLines = 1)
              Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
          }
          DropdownMenu(
            expanded = categoryExpanded,
            onDismissRequest = { categoryExpanded = false }
          ) {
            categories.forEach { cat ->
              DropdownMenuItem(
                text = { Text(cat) },
                onClick = {
                  selectedCategory = cat
                  categoryExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Document Type Picker
        Text(
          text = LanguageStrings.get("doc_type_label", currentLanguage),
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedButton(
            onClick = { docExpanded = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("select_doc_type_btn")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = selectedDocType, maxLines = 1)
              Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
          }
          DropdownMenu(
            expanded = docExpanded,
            onDismissRequest = { docExpanded = false }
          ) {
            docTypes.forEach { doc ->
              DropdownMenuItem(
                text = { Text(doc) },
                onClick = {
                  selectedDocType = doc
                  docExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Known As
        OutlinedTextField(
          value = knownAsText,
          onValueChange = { knownAsText = it },
          label = { Text(if (isNepali) "चिनिने नाम (Known As)" else "Known As / Public Name") },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("verification_known_as_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Official Link / Social Presence
        OutlinedTextField(
          value = linkText,
          onValueChange = { linkText = it },
          label = { Text(if (isNepali) "वेबसाइट वा मिडिया लिङ्क" else "Official Website / Social Link") },
          placeholder = { Text("https://example.com/profile") },
          leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
          shape = RoundedCornerShape(12.dp),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("verification_link_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Reason / Justification
        OutlinedTextField(
          value = reasonText,
          onValueChange = { reasonText = it },
          label = { Text(LanguageStrings.get("reason_label", currentLanguage)) },
          placeholder = {
            Text(
              if (isNepali) "तपाईंको काम, समुदायमा योगदान, र परिचय प्रमाणको आधार..."
              else "Describe your public presence, work, and why authenticity matters..."
            )
          },
          shape = RoundedCornerShape(12.dp),
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("verification_reason_input")
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Notice: Review by CEO Bir Pujara
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(VerifiedGreen.copy(alpha = 0.09f))
            .padding(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = VerifiedGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isNepali) "यो आवेदन समीक्षा र प्रमाणीकरणका लागि सिधा सिईओ वीर पुजारालाई पठाइनेछ।"
            else "This application is securely submitted to CEO Bir Pujara for authorized review.",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.onSurface,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("cancel_verification_btn")
          ) {
            Text(LanguageStrings.get("cancel", currentLanguage))
          }

          Button(
            onClick = {
              if (reasonText.isBlank()) {
                Toast.makeText(
                  context,
                  if (isNepali) "कृपया प्रमाणीकरणको कारण खुलाउनुहोस्" else "Please explain why your account should be verified",
                  Toast.LENGTH_SHORT
                ).show()
                return@Button
              }
              isSubmitting = true
              onSubmit(selectedCategory, selectedDocType, reasonText, knownAsText, linkText)
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = FacebookVerifiedBlue
            ),
            modifier = Modifier
              .weight(1.5f)
              .testTag("submit_verification_btn")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isNepali) "आवेदन पेश गर्नुहोस्" else "Submit Application",
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
