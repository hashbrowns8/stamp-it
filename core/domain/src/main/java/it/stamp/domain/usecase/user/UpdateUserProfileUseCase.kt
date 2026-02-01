package it.stamp.domain.usecase.user

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.domain.service.UserProfileService
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService,
    private val userProfileService: UserProfileService,
) {
    suspend operator fun invoke(
        avatar: Avatar,
        displayName: DisplayName,
    ): Result<Unit> = runCatching {
        currentUserContextService.requireUser()
            .let { user ->
                userProfileService.updateProfile(
                    user.id,
                    avatar = if (user.avatar == avatar) {
                        null
                    } else {
                        avatar
                    },
                    displayName = if (user.displayName == displayName) {
                        null
                    } else {
                        displayName
                    },
                )
            }
    }
}