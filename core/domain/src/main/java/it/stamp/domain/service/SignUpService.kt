package it.stamp.domain.service

import it.stamp.model.ids.UserId
import it.stamp.model.user.User

interface SignUpService {
    suspend fun signUp(id: UserId): User
}