package com.kmptemplate.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kmptemplate.app.ui.AuthScreen

@SuppressLint("Registered")
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    setContent {
      AuthScreen()
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  AuthScreen()
}
