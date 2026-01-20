package it.stamp.domain.usecase.user

import it.stamp.domain.exception.UnauthorizedException
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.UserAndGroupProfileService
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val authService: AuthService,
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val profileService: UserAndGroupProfileService,
) {
    suspend operator fun invoke(command: EditProfileCommand) =
        runCatching {
            with(command) {
                if (user == null && group == null) return@runCatching

                if (user != null) {
                    val currentUser = authService.requireCurrentUser()

                    require(user.id == currentUser.id) {
                        throw UnauthorizedException()
                    }
                }

                if (group != null) {
                    val membership = getMyMembershipUseCase()
                        .getOrThrow()

                    require(group.id == membership.groupId && membership.isLeader) {
                        throw UnauthorizedException()
                    }
                }

                profileService.update(user, group)
            }
        }
}

data class EditProfileCommand(val user: User?, val group: Group?)