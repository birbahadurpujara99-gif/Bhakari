package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppLanguage
import com.example.model.Post
import com.example.ui.theme.BhakariCrimson

@Composable
fun ReportContentDialog(
  post: Post,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onSubmitReport: (reason: String) -> Unit
) {
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var selectedPreset by remember { mutableStateOf("Inappropriate Content") }
  var customDetails by remember { mutableStateOf("") }
  val reasons = listOf("Inappropriate Content", "Spam / Misleading", "Harassment", "Hate Speech", "Copyright Violation")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = if (isNepali) "सामग्री रिपोर्ट गर्नुहोस्" else "Report Content",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BhakariCrimson
          )
        )
        Text(
          text = if (isNepali) "सिईओ र मोडरेटर टोलीले समीक्षा गर्नेछ।" else "Sent to CEO Bir Pujara and Moderation Team for review.",
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(14.dp))

        reasons.forEach { reason ->
          FilterChip(
            selected = selectedPreset == reason,
            onClick = { selectedPreset = reason },
            label = { Text(reason, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BhakariCrimson.copy(alpha = 0.15f),
              selectedLabelColor = BhakariCrimson
            ),
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = customDetails,
          onValueChange = { customDetails = it },
          placeholder = { Text(if (isNepali) "थप विवरण (वैकल्पिक)..." else "Additional details (optional)...") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          minLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
            Text("Cancel")
          }
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            onClick = {
              val finalReason = if (customDetails.isNotBlank()) "$selectedPreset: $customDetails" else selectedPreset
              onSubmitReport(finalReason)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BhakariCrimson)
          ) {
            Text(if (isNepali) "रिपोर्ट पेश गर्नुहोस्" else "Submit Report", color = Color.White)
          }
        }
      }
    }
  }
}
