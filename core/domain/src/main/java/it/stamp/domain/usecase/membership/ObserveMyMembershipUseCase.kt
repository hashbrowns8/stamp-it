package it.stamp.domain.usecase.membership

import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.usecase.user.ObserveAuthenticatedUserUseCase
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMembershipUseCase @Inject constructor(
    private val observeAuthenticatedUserUseCase: ObserveAuthenticatedUserUseCase,
    private val membershipRepository: MembershipRepository,
) {
    operator fun invoke(): Flow<Membership> = observeAuthenticatedUserUseCase()
        .filterNotNull()
        .distinctUntilChangedBy { it.id }
        .flatMapLatest { user ->
            membershipRepository.observeUserMembership(user.id)
                .map { membership ->
                    membership ?: throw MembershipNotFoundException()
                }
        }
}