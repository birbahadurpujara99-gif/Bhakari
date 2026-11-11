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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import com.example.ui.theme.FacebookVerifiedBlue
import com.example.ui.theme.VerifiedGreen

@Composable
fun AuthScreen(
  currentLanguage: AppLanguage,
  onToggleLanguage: () -> Unit,
  onLoginGoogle: () -> Unit,
  onLoginPhone: (String, String) -> Unit,
  onLoginEmail: (String, String) -> Unit,
  onSignupEmail: (String, String, String) -> Unit,
  onLoginAsCeo: () -> Unit,
  onContinueAsGuest: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isNepali = currentLanguage == AppLanguage.NEPALI
  val isHindi = currentLanguage == AppLanguage.HINDI
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Phone, 1: Email, 2: Signup

  // Form Fields
  var phoneNumber by remember { mutableStateOf("9841234567") }
  var phoneOtp by remember { mutableStateOf("") }
  var otpSent by remember { mutableStateOf(false) }
  
  var emailAddress by remember { mutableStateOf("birbahadurpujara99@gmail.com") }
  var emailPassword by remember { mutableStateOf("Pujara@123") }
  var isPasswordVisible by remember { mutableStateOf(false) }
  
  var signupFullName by remember { mutableStateOf("Suman Thapa") }
  var signupEmail by remember { mutableStateOf("suman.thapa@bhakari.np") }
  var signupPassword by remember { mutableStateOf("bhakariPass123") }
  
  var showForgotPasswordDialog by remember { mutableStateOf(false) }
  var forgotPasswordEmail by remember { mutableStateOf("") }

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
      // Top Language Switcher Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        OutlinedButton(
          onClick = onToggleLanguage,
          shape = RoundedCornerShape(20.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.testTag("auth_language_switch_button")
        ) {
          Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${currentLanguage.flag} ${currentLanguage.displayName}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bhakari Logo with letter B
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(86.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(BhakariGold, BhakariCrimson, FacebookVerifiedBlue)
            )
          )
          .padding(3.dp)
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Bhakari Logo",
            modifier = Modifier.size(72.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = LanguageStrings.get("app_name", currentLanguage),
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface,
          letterSpacing = 1.sp
        )
      )

      Text(
        text = LanguageStrings.get("app_tagline", currentLanguage),
        style = MaterialTheme.typography.bodySmall.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium
        )
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Google Sign-In Primary Button
      Button(
        onClick = onLoginGoogle,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("google_signin_button")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Google Colorful G Icon Placeholder
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(Color(0xFF4285F4)),
            contentAlignment = Alignment.Center
          ) {
            Text("G", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = LanguageStrings.get("login_google", currentLanguage),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tabs: Phone / Email / Signup
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = FacebookVerifiedBlue,
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .padding(4.dp),
        indicator = { tabPositions ->
          if (selectedTab < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier
                .tabIndicatorOffset(tabPositions[selectedTab])
                .clip(RoundedCornerShape(12.dp)),
              color = FacebookVerifiedBlue,
              height = 3.dp
            )
          }
        }
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text(LanguageStrings.get("tab_phone", currentLanguage), fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
          modifier = Modifier.testTag("tab_phone_login")
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text(LanguageStrings.get("tab_email", currentLanguage), fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
          modifier = Modifier.testTag("tab_email_login")
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text(LanguageStrings.get("tab_signup", currentLanguage), fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
          modifier = Modifier.testTag("tab_signup_register")
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Tab 0: Phone Number with OTP verification
      AnimatedVisibility(visible = selectedTab == 0) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(LanguageStrings.get("phone_label", currentLanguage)) },
            placeholder = { Text("+977 98XXXXXXXX") },
            leadingIcon = {
              Icon(Icons.Default.Phone, contentDescription = null, tint = FacebookVerifiedBlue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("phone_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          if (!otpSent) {
            Button(
              onClick = {
                if (phoneNumber.isNotBlank()) {
                  otpSent = true
                  phoneOtp = "554422" // Demo verification code generated
                  Toast.makeText(context, "Verification OTP code sent to $phoneNumber!", Toast.LENGTH_LONG).show()
                } else {
                  Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
                }
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("send_otp_button")
            ) {
              Text(
                text = LanguageStrings.get("send_otp", currentLanguage),
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          } else {
            OutlinedTextField(
              value = phoneOtp,
              onValueChange = { phoneOtp = it },
              label = { Text(LanguageStrings.get("otp_label", currentLanguage)) },
              placeholder = { Text("6-digit code") },
              leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = FacebookVerifiedBlue)
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("otp_input_field"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = {
                if (phoneOtp.isNotBlank()) {
                  onLoginPhone(phoneNumber, phoneOtp)
                } else {
                  Toast.makeText(context, "Please enter verification OTP", Toast.LENGTH_SHORT).show()
                }
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("verify_otp_button")
            ) {
              Text(
                text = LanguageStrings.get("verify_otp", currentLanguage),
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }

      // Tab 1: Email & Password Login
      AnimatedVisibility(visible = selectedTab == 1) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          OutlinedTextField(
            value = emailAddress,
            onValueChange = { emailAddress = it },
            label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
            placeholder = { Text("name@example.com") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null, tint = FacebookVerifiedBlue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("email_input_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = emailPassword,
            onValueChange = { emailPassword = it },
            label = { Text(LanguageStrings.get("password_label", currentLanguage)) },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = FacebookVerifiedBlue)
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

          // Forgot Password Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
          ) {
            TextButton(
              onClick = {
                forgotPasswordEmail = emailAddress
                showForgotPasswordDialog = true
              },
              modifier = Modifier.testTag("forgot_password_btn")
            ) {
              Text(
                text = LanguageStrings.get("forgot_password", currentLanguage),
                color = FacebookVerifiedBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = {
              if (emailAddress.isNotBlank() && emailPassword.isNotBlank()) {
                onLoginEmail(emailAddress, emailPassword)
              } else {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
              }
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("email_login_submit_button")
          ) {
            Text(
              text = LanguageStrings.get("login_btn", currentLanguage),
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontSize = 15.sp
            )
          }
        }
      }

      // Tab 2: Create Account / Signup
      AnimatedVisibility(visible = selectedTab == 2) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          OutlinedTextField(
            value = signupFullName,
            onValueChange = { signupFullName = it },
            label = { Text(LanguageStrings.get("fullname_label", currentLanguage)) },
            placeholder = { Text("Suman Thapa") },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = FacebookVerifiedBlue)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_name_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = signupEmail,
            onValueChange = { signupEmail = it },
            label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
            placeholder = { Text("suman@bhakari.np") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null, tint = FacebookVerifiedBlue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_email_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = signupPassword,
            onValueChange = { signupPassword = it },
            label = { Text(LanguageStrings.get("password_label", currentLanguage)) },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = FacebookVerifiedBlue)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("signup_password_field"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              if (signupEmail.isNotBlank() && signupPassword.isNotBlank()) {
                onSignupEmail(signupFullName, signupEmail, signupPassword)
              } else {
                Toast.makeText(context, "Please enter all required fields", Toast.LENGTH_SHORT).show()
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

      Spacer(modifier = Modifier.height(18.dp))

      // Direct CEO Bir Pujara One-Tap Access
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = FacebookVerifiedBlue.copy(alpha = 0.09f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.horizontalGradient(listOf(FacebookVerifiedBlue, BhakariGold))
        ),
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
              .size(46.dp)
              .clip(CircleShape)
              .border(2.dp, FacebookVerifiedBlue, CircleShape)
          ) {
            Image(
              painter = painterResource(id = R.drawable.ceo_bir_pujara),
              contentDescription = "CEO Bir Pujara",
              modifier = Modifier.size(46.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Bir Pujara (CEO)",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              Spacer(modifier = Modifier.width(4.dp))
              VerifiedBadge(size = 15.dp, isCeo = true)
            }
            Text(
              text = if (isNepali) "आधिकारिक सिईओ लगइन र प्रशासनिक ड्यासबोर्ड" else "Official CEO Account with Full Administrative Control",
              style = MaterialTheme.typography.bodySmall.copy(
                color = FacebookVerifiedBlue,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

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

  // Forgot Password Dialog
  if (showForgotPasswordDialog) {
    AlertDialog(
      onDismissRequest = { showForgotPasswordDialog = false },
      title = { Text(LanguageStrings.get("forgot_password", currentLanguage), fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(
            if (isNepali) "पासवर्ड रिसेट लिङ्क प्राप्त गर्न आफ्नो दर्ता गरिएको इमेल ठेगाना लेख्नुहोस्:"
            else "Enter your registered email address to receive password reset instructions:"
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = forgotPasswordEmail,
            onValueChange = { forgotPasswordEmail = it },
            label = { Text(LanguageStrings.get("email_label", currentLanguage)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showForgotPasswordDialog = false
            Toast.makeText(context, "Password reset instructions sent to $forgotPasswordEmail!", Toast.LENGTH_LONG).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = FacebookVerifiedBlue)
        ) {
          Text(LanguageStrings.get("reset_password", currentLanguage))
        }
      },
      dismissButton = {
        TextButton(onClick = { showForgotPasswordDialog = false }) {
          Text(LanguageStrings.get("cancel", currentLanguage))
        }
      }
    )
  }
}
