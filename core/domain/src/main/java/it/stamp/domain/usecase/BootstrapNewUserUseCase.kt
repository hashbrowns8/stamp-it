package it.stamp.domain.usecase

import it.stamp.domain.service.UserBootstrapService
import it.stamp.domain.util.DisplayNameGenerator
import it.stamp.domain.util.InviteCodeGenerator
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.membership.Role
import it.stamp.model.user.User
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Singleton
class BootstrapNewUserUseCase @Inject constructor(
    private val bootstrapService: UserBootstrapService,
) {
    suspend operator fun invoke(user: User) = withContext(Dispatchers.IO) {
        val displayName = DisplayNameGenerator.generate(user.id.value)

        val user = user.copy(displayName = displayName)

        val inviteCode = InviteCodeGenerator.generate()

        val group = Group(
            id = GroupId(Uuid.random().toString()),
            name = "${displayName}의 그룹",
            inviteCode,
            createdAt = Clock.System.now(),
        )

        val membership = Membership(
            id = MembershipId("${group.id.value}_${user.id.value}"),
            groupId = group.id,
            userId = user.id,
            role = Role.LEADER,
            joinedAt = Clock.System.now(),
        )

        bootstrapService.bootstrap(user, group, membership)
    }
}