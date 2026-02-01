package it.stamp.domain.service

import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName

interface UserProfileService {
    suspend fun updateProfile(id: UserId, avatar: Avatar?, displayName: DisplayName?)
}