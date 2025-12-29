package it.stamp.domain.usecase

import it.stamp.domain.generator.DisplayNameGenerator
import it.stamp.domain.generator.InviteCodeGenerator
import it.stamp.domain.service.UserBootstrapService
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.membership.Role
import it.stamp.model.user.User
import jakarta.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Singleton
class BootstrapNewUserUseCase @Inject constructor(
    private val bootstrapService: UserBootstrapService,
) {
    suspend operator fun invoke(user: User): Result<Unit> {
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

        return bootstrapService.bootstrap(user, group, membership)
    }
}