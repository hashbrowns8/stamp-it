package it.stamp.domain.usecase

import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMembershipByUserUseCase @Inject constructor(
    private val membershipRepository: MembershipRepository,
) {
    operator fun invoke(userId: UserId): Flow<Membership?> =
        membershipRepository.observeMembershipByUser(userId)
}