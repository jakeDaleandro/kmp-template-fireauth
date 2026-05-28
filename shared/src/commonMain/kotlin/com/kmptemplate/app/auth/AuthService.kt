package com.kmptemplate.app.auth

expect class AuthService() {
  suspend fun signInWithEmail(email: String, password: String): Any?
  suspend fun signUpWithEmail(email: String, password: String): Any?
  suspend fun signOut(): Unit
  fun currentUser(): Any?
}
