package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AppLanguage
import com.example.data.LanguageStrings
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.BhakariCrimson
import com.example.ui.theme.BhakariGold
import com.example.ui.theme.BhakariGoldDark
import com.example.ui.theme.VerifiedGreen

@Composable
fun AuthScreen(
  currentLanguage: AppLanguage,
  onToggleLanguage: () -> Unit,
  onLoginPhone: (String, String) -> Unit,
  onLoginEmail: (String, String) -> Unit,
  onSignupEmail: (String, String, String) -> Unit,
  onLoginAsCeo: () -> Unit,
  onContinueAsGuest: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Phone, 1: Email, 2: Signup

  // Form Fields
  var phoneNumber by remember { mutableStateOf("9841234567") }
  var phoneOtp by remember { mutableStateOf("1234") }
  var emailAddress by remember { mutableStateOf("suman.thapa@bhakari.np") }
  var emailPassword by remember { mutableStateOf("bhakari123") }
  var signupFullName by remember { mutableStateOf("Sunita Karki") }
  var signupEmail by remember { mutableStateOf("sunita@bhakari.np") }
  var signupPassword by remember { mutableStateOf("securePass123") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .imePadding()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top bar with Language Switcher
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(BhakariGold.copy(alpha = 0.15f))
            .border(1.dp, BhakariGold, RoundedCornerShape(20.dp))
            .clickable { onToggleLanguage() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("auth_language_switch_button"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (isNepali) "🇳🇵 नेपाली (Switch to EN)" else "🇬🇧 English (नेपालीमा हेर्नुहोस्)",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bhakari Brand Logo & Title
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(86.dp)
          .clip(RoundedCornerShape(22.dp))
          .border(2.5.dp, BhakariGold, RoundedCornerShape(22.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.bhakari_logo),
          contentDescription = "Bhakari Logo",
          modifier = Modifier.size(80.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Bhakari",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            color = BhakariGold
          )
        )
        Text(
          text = " | भकारी",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = BhakariCrimson
          )
        )
      }

      Text(
        text = LanguageStrings.get("login_subtitle", currentLanguage),
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Tab selector: Phone Number / Email Account / Sign Up
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.Transparent,
          contentColor = BhakariGold,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = BhakariGold,
              height = 3.dp
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = {
              Text(
                text = LanguageStrings.get("tab_phone", currentLanguage),
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            icon = {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
            },
            modifier = Modifier.testTag("tab_phone_login")
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = {
              Text(
                text = LanguageStrings.get("tab_email", currentLanguage),
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            icon = {
              Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
            },
            modifier = Modifier.testTag("tab_email_login")
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = {
              Text(
                text = LanguageStrings.get("tab_signup", currentLanguage),
                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
              )
            },
            icon = {
              Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp))
            },
            modifier = Modifier.testTag("tab_signup")
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Tab 0: Phone Number Login
      AnimatedVisibility(visible = selectedTab == 0) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = LanguageStrings.get("phone_login_desc", currentLanguage),
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 12.dp)
          )

          OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(LanguageStrings.get("phone_label", currentLanguage)) },
            placeholder = { Text("+977 98XXXXXXXX") },
            leadingIcon = {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 6.dp)
              ) {
                Text(text = "🇳🇵 +977", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("phone_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = phoneOtp,
            onValueChange = { phoneOtp = it },
            label = { Text(if (isNepali) "ओटीपी कोड (OTP)" else "Verification Code (OTP)") },
            placeholder = { Text("1234") },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = BhakariGold)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("otp_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              if (phoneNumber.isNotBlank()) {
                onLoginPhone(phoneNumber, phoneOtp)
              } else {
                Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BhakariGold),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("phone_login_submit_button")
          ) {
            Text(
              text = LanguageStrings.get("login_btn", currentLanguage),
              fontWeight = FontWeight.Bold,
              color = Color.Black,
              fontSize = 15.sp
            )
          }
        }
      }

      // Tab 1: Email Account Login
      AnimatedVisibility(visible = selectedTab == 1) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = LanguageStrings.get("email_login_desc", currentLanguage),
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 12.dp)
          )

          OutlinedTextField(
            value = emailAddress,
            onValueChange = { emailAddress = it },
            label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
            placeholder = { Text("name@example.com") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null, tint = BhakariGold)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("email_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = emailPassword,
            onValueChange = { emailPassword = it },
            label = { Text(LanguageStrings.get("password_label", currentLanguage)) },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = BhakariGold)
            },
            trailingIcon = {
              IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                  imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = null
                )
              }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("password_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              if (emailAddress.isNotBlank()) {
                onLoginEmail(emailAddress, emailPassword)
              } else {
                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BhakariGold),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("email_login_submit_button")
          ) {
            Text(
              text = LanguageStrings.get("login_btn", currentLanguage),
              fontWeight = FontWeight.Bold,
              color = Color.Black,
              fontSize = 15.sp
            )
          }
        }
      }

      // Tab 2: Signup with Email
      AnimatedVisibility(visible = selectedTab == 2) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = LanguageStrings.get("signup_desc", currentLanguage),
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 12.dp)
          )

          OutlinedTextField(
            value = signupFullName,
            onValueChange = { signupFullName = it },
            label = { Text(LanguageStrings.get("fullname_label", currentLanguage)) },
            placeholder = { Text("Sunita Karki") },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = BhakariCrimson)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_name_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = signupEmail,
            onValueChange = { signupEmail = it },
            label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
            placeholder = { Text("sunita@example.com") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null, tint = BhakariCrimson)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_email_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = signupPassword,
            onValueChange = { signupPassword = it },
            label = { Text(LanguageStrings.get("password_label", currentLanguage)) },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = BhakariCrimson)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_password_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              if (signupEmail.isNotBlank()) {
                onSignupEmail(signupFullName, signupEmail, signupPassword)
              } else {
                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BhakariCrimson),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("signup_submit_button")
          ) {
            Text(
              text = LanguageStrings.get("signup_btn", currentLanguage),
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontSize = 15.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Divider
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
          text = " ${LanguageStrings.get("or_divider", currentLanguage)} ",
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Direct CEO Login Shortcut (Prominently requested: CEO Bir Pujara)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = VerifiedGreen.copy(alpha = 0.1f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(VerifiedGreen, BhakariGold))),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onLoginAsCeo() }
          .testTag("ceo_demo_login_card")
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
              .border(2.dp, VerifiedGreen, CircleShape)
          ) {
            Image(
              painter = painterResource(id = R.drawable.ceo_bir_pujara),
              contentDescription = "CEO Bir Pujara",
              modifier = Modifier.size(44.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (isNepali) "सिईओ वीर पुजाराको रूपमा लगइन" else "Sign In as CEO Bir Pujara",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              Spacer(modifier = Modifier.width(4.dp))
              VerifiedBadge(size = 14.dp)
            }
            Text(
              text = if (isNepali) "आधिकारिक सिईओ प्रोफाइल र पोस्ट पहुँच" else "Official CEO Verified Account Access",
              style = MaterialTheme.typography.bodySmall.copy(
                color = VerifiedGreen,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Continue as Guest Button
      OutlinedButton(
        onClick = onContinueAsGuest,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("continue_as_guest_button"),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(
          text = LanguageStrings.get("guest_btn", currentLanguage),
          style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
