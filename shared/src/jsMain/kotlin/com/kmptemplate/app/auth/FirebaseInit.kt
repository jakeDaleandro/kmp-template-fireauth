package com.kmptemplate.app.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun initializeFirebase() {
  Firebase.initialize(
    options = FirebaseOptions(
      applicationId = "1:326117768912:web:a0f4f2a7c3993ee6f9a8d5",  // appId
      apiKey = "AIzaSyCdXcaQJ3VSHQ2y_jEbprvZcyUcEvzAS14",
      projectId = "wnkk-9486c",
      storageBucket = "wnkk-9486c.firebasestorage.app",
      gcmSenderId = "326117768912",  // messagingSenderId
      authDomain = "wnkk-9486c.firebaseapp.com"
    )
  )
}
