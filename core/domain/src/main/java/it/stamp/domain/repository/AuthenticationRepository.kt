package it.stamp.domain.repository

import it.stamp.model.user.User

interface AuthenticationRepository {
    suspend fun signInWithGoogle(): Result<User>
}