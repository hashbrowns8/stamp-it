package it.stamp.domain.usecase.membership

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMembershipUseCase @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val membershipRepository: MembershipRepository,
) {
    operator fun invoke(): Flow<Membership?> = observeCurrentUserUseCase()
        .distinctUntilChangedBy { it?.id }
        .flatMapLatest { user ->
            user?.id
                ?.let(membershipRepository::observeUserMembership)
                ?: flowOf(null)
        }
}