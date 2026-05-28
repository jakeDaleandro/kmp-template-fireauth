package com.kmptemplate.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmptemplate.app.auth.AuthService
import kotlinx.coroutines.launch

// ── Palette ──────────────────────────────────────────────────────────────────
private val BgDeep    = Color(0xFF0E0E0F)
private val BgSurface = Color(0xFF1A1A1C)
private val BgField   = Color(0xFF242427)
private val Cream     = Color(0xFFF5F0E8)
private val CreamMid  = Color(0xFFB8B0A0)
private val CreamDim  = Color(0xFF6B6560)
private val Accent    = Color(0xFFD4A853)   // warm gold
private val AccentDim = Color(0xFF8A6A2E)
private val Danger    = Color(0xFFE05555)

// ── Screen state ─────────────────────────────────────────────────────────────
private enum class AuthMode { LOGIN, REGISTER, SUCCESS }

@Composable
fun AuthScreen() {
  var mode by remember { mutableStateOf(AuthMode.LOGIN) }
  var successMessage by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(BgDeep),
    contentAlignment = Alignment.Center
  ) {
    AnimatedContent(
      targetState = mode,
      transitionSpec = {
        if (targetState == AuthMode.SUCCESS) {
          fadeIn() togetherWith fadeOut()
        } else {
          val dir = if (targetState == AuthMode.REGISTER) 1 else -1
          slideInHorizontally { dir * it } togetherWith slideOutHorizontally { -dir * it }
        }
      }
    ) { target ->
      when (target) {
        AuthMode.LOGIN -> LoginPane(
          onSwitchToRegister = { mode = AuthMode.REGISTER },
          onSuccess = {
            successMessage = "Welcome back."
            mode = AuthMode.SUCCESS
          }
        )
        AuthMode.REGISTER -> RegisterPane(
          onSwitchToLogin = { mode = AuthMode.LOGIN },
          onSuccess = {
            successMessage = "Account created."
            mode = AuthMode.SUCCESS
          }
        )
        AuthMode.SUCCESS -> SuccessPane(
          message = successMessage,
          onContinue = { mode = AuthMode.LOGIN }
        )
      }
    }
  }
}

// ── Shared card shell ─────────────────────────────────────────────────────────
@Composable
private fun AuthCard(content: @Composable () -> Unit) {
  Box(
    modifier = Modifier
      .width(380.dp)
      .clip(RoundedCornerShape(2.dp))
      .background(BgSurface)
      .border(0.5.dp, CreamDim.copy(alpha = 0.25f), RoundedCornerShape(2.dp))
      .padding(40.dp)
  ) {
    content()
  }
}

// ── Wordmark ──────────────────────────────────────────────────────────────────
@Composable
private fun Wordmark() {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .background(Accent)
    )
    Spacer(Modifier.width(10.dp))
    Text(
      text = "KMPTEMPLATE",
      color = Cream,
      fontSize = 11.sp,
      fontWeight = FontWeight.W600,
      letterSpacing = 3.sp
    )
  }
}

// ── Shared field ─────────────────────────────────────────────────────────────
@Composable
private fun AuthField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  isPassword: Boolean = false,
  isEmail: Boolean = false
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    placeholder = {
      Text(placeholder, color = CreamDim, fontSize = 14.sp)
    },
    singleLine = true,
    visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions = KeyboardOptions(
      keyboardType = if (isEmail) KeyboardType.Email else if (isPassword) KeyboardType.Password else KeyboardType.Text
    ),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = Accent,
      unfocusedBorderColor = CreamDim.copy(alpha = 0.3f),
      focusedTextColor = Cream,
      unfocusedTextColor = Cream,
      cursorColor = Accent,
      focusedContainerColor = BgField,
      unfocusedContainerColor = BgField
    ),
    shape = RoundedCornerShape(2.dp),
    modifier = Modifier.fillMaxWidth()
  )
}

// ── Primary button ────────────────────────────────────────────────────────────
@Composable
private fun AuthButton(
  label: String,
  loading: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp)
      .clip(RoundedCornerShape(2.dp))
      .background(Accent)
      .clickable(enabled = !loading, onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    if (loading) {
      CircularProgressIndicator(
        modifier = Modifier.size(20.dp),
        color = BgDeep,
        strokeWidth = 2.dp
      )
    } else {
      Text(
        text = label,
        color = BgDeep,
        fontSize = 13.sp,
        fontWeight = FontWeight.W600,
        letterSpacing = 1.5.sp
      )
    }
  }
}

// ── Toggle link ───────────────────────────────────────────────────────────────
@Composable
private fun ToggleLink(prefix: String, action: String, onClick: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    Text(prefix, color = CreamDim, fontSize = 13.sp)
    Spacer(Modifier.width(4.dp))
    Text(
      text = action,
      color = Accent,
      fontSize = 13.sp,
      modifier = Modifier.clickable(onClick = onClick)
    )
  }
}

// ── LOGIN PANE ────────────────────────────────────────────────────────────────
@Composable
private fun LoginPane(
  onSwitchToRegister: () -> Unit,
  onSuccess: () -> Unit
) {
  val auth = remember { AuthService() }
  val scope = rememberCoroutineScope()

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var error by remember { mutableStateOf("") }
  var loading by remember { mutableStateOf(false) }

  AuthCard {
    Column {
      Wordmark()
      Spacer(Modifier.height(32.dp))

      Text("Sign in", color = Cream, fontSize = 24.sp, fontWeight = FontWeight.W300)
      Spacer(Modifier.height(4.dp))
      Text("Enter your credentials to continue", color = CreamDim, fontSize = 13.sp)
      Spacer(Modifier.height(28.dp))

      AuthField(email, { email = it }, "Email address", isEmail = true)
      Spacer(Modifier.height(12.dp))
      AuthField(password, { password = it }, "Password", isPassword = true)

      if (error.isNotEmpty()) {
        Spacer(Modifier.height(10.dp))
        Text(error, color = Danger, fontSize = 12.sp)
      }

      Spacer(Modifier.height(24.dp))
      AuthButton("SIGN IN", loading) {
        if (email.isBlank() || password.isBlank()) {
          error = "Please fill in all fields."
          return@AuthButton
        }
        scope.launch {
          loading = true
          error = ""
          try {
            auth.signInWithEmail(email, password)
            onSuccess()
          } catch (e: Exception) {
            error = e.message ?: "Sign in failed."
          } finally {
            loading = false
          }
        }
      }

      Spacer(Modifier.height(20.dp))
      ToggleLink("Don't have an account?", "Create one", onSwitchToRegister)
    }
  }
}

// ── REGISTER PANE ─────────────────────────────────────────────────────────────
@Composable
private fun RegisterPane(
  onSwitchToLogin: () -> Unit,
  onSuccess: () -> Unit
) {
  val auth = remember { AuthService() }
  val scope = rememberCoroutineScope()

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirm by remember { mutableStateOf("") }
  var error by remember { mutableStateOf("") }
  var loading by remember { mutableStateOf(false) }

  AuthCard {
    Column {
      Wordmark()
      Spacer(Modifier.height(32.dp))

      Text("Create account", color = Cream, fontSize = 24.sp, fontWeight = FontWeight.W300)
      Spacer(Modifier.height(4.dp))
      Text("Get started — it only takes a moment", color = CreamDim, fontSize = 13.sp)
      Spacer(Modifier.height(28.dp))

      AuthField(email, { email = it }, "Email address", isEmail = true)
      Spacer(Modifier.height(12.dp))
      AuthField(password, { password = it }, "Password", isPassword = true)
      Spacer(Modifier.height(12.dp))
      AuthField(confirm, { confirm = it }, "Confirm password", isPassword = true)

      if (error.isNotEmpty()) {
        Spacer(Modifier.height(10.dp))
        Text(error, color = Danger, fontSize = 12.sp)
      }

      Spacer(Modifier.height(24.dp))
      AuthButton("CREATE ACCOUNT", loading) {
        when {
          email.isBlank() || password.isBlank() || confirm.isBlank() ->
            error = "Please fill in all fields."
          password != confirm ->
            error = "Passwords do not match."
          password.length < 6 ->
            error = "Password must be at least 6 characters."
          else -> scope.launch {
            loading = true
            error = ""
            try {
              auth.signUpWithEmail(email, password)
              onSuccess()
            } catch (e: Exception) {
              error = e.message ?: "Registration failed."
            } finally {
              loading = false
            }
          }
        }
      }

      Spacer(Modifier.height(20.dp))
      ToggleLink("Already have an account?", "Sign in", onSwitchToLogin)
    }
  }
}

// ── SUCCESS PANE ──────────────────────────────────────────────────────────────
@Composable
private fun SuccessPane(
  message: String,
  onContinue: () -> Unit
) {
  AuthCard {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Spacer(Modifier.height(8.dp))

      // Gold square accent
      Box(
        modifier = Modifier
          .size(48.dp)
          .border(1.dp, Accent, RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(12.dp)
            .background(Accent)
        )
      }

      Spacer(Modifier.height(28.dp))
      Text(
        text = message,
        color = Cream,
        fontSize = 24.sp,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text = "You're all set.",
        color = CreamDim,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(36.dp))
      AuthButton("CONTINUE", false, onContinue)
    }
  }
}
