package com.kmptemplate.app.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

actual class AuthService actual constructor() {
  private val auth = Firebase.auth

  actual suspend fun signInWithEmail(email: String, password: String): Any? =
    auth.signInWithEmailAndPassword(email, password)

  actual suspend fun signUpWithEmail(email: String, password: String): Any? =
    auth.createUserWithEmailAndPassword(email, password)

  actual suspend fun signOut(): Unit = auth.signOut()

  actual fun currentUser(): Any? = auth.currentUser
}
