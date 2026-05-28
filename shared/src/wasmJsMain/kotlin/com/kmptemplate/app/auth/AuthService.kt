package com.kmptemplate.app.auth

actual class AuthService actual constructor() {
  actual suspend fun signInWithEmail(email: String, password: String): Any? = null

  actual suspend fun signUpWithEmail(email: String, password: String): Any? = null

  actual suspend fun signOut(): Unit { /* no-op on WASM */ }

  actual fun currentUser(): Any? = null
}
