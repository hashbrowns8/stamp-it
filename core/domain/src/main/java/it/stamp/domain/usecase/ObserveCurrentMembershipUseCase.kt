package it.stamp.domain.usecase

import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveCurrentMembershipUseCase @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val membershipRepository: MembershipRepository,
) {
    operator fun invoke(): Flow<Membership?> = observeCurrentUserUseCase()
        .flatMapLatest { user ->
            user?.id
                ?.let { userId -> membershipRepository.observeMembershipByUser(userId) }
                ?: flowOf(null)
        }
}