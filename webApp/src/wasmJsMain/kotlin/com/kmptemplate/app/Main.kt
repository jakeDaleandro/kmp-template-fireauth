package com.kmptemplate.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kmptemplate.app.ui.AuthScreen

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  ComposeViewport {
    AuthScreen()
  }
}
