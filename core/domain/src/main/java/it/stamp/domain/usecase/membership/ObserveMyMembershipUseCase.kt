package it.stamp.domain.usecase.membership

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.usecase.user.ObserveAuthenticationState
import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMembershipUseCase @Inject constructor(
    private val observeAuthenticationState: ObserveAuthenticationState,
    private val membershipRepository: MembershipRepository,
) {
    operator fun invoke(): Flow<Membership?> = observeAuthenticationState()
        .map { state ->
            when (state) {
                is AuthenticationState.Authenticated -> state.userId
                else -> null
            }
        }
        .flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                membershipRepository.observeUserMembership(id)
            }
        }
}