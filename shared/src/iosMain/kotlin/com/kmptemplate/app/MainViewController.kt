package com.kmptemplate.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.kmptemplate.app.ui.AuthScreen

fun mainViewController() = ComposeUIViewController {
  Box(modifier = Modifier.fillMaxSize()) {
    AuthScreen()
  }
}
