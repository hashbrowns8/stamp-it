package it.stamp.data.service

import it.stamp.common.di.ApplicationScope
import it.stamp.common.di.IODispatcher
import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.CurrentUserContextService
import it.stamp.domain.service.MemberService
import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class DefaultCurrentUserContextService @Inject constructor(
    authenticationService: AuthenticationService,
    private val userRepository: UserRepository,
    private val membershipRepository: MembershipRepository,
    private val groupRepository: GroupRepository,
    private val memberService: MemberService,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    @ApplicationScope coroutineScope: CoroutineScope,
) : CurrentUserContextService {

    private val user: StateFlow<User?> = authenticationService.observeAuthenticationState()
        .flatMapLatest { state ->
            when (state) {
                is AuthenticationState.Authenticated -> userRepository.observe(state.userId)
                else -> flowOf(null)
            }
        }
        .flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun observeUser(): Flow<User?> = user

    private val membership: StateFlow<Membership?> = user.map { user -> user?.id }
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            if (userId == null) {
                flowOf(null)
            } else {
                membershipRepository.observeUserMembership(userId)
            }
        }
        .flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun observeMembership(): Flow<Membership?> = membership

    private val group: StateFlow<Group?> = membership.map { membership -> membership?.groupId }
        .distinctUntilChanged()
        .flatMapLatest { groupId ->
            if (groupId == null) {
                flowOf(null)
            } else {
                groupRepository.observe(groupId)
            }
        }
        .flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override fun observeGroup(): Flow<Group?> = group

    private val members: StateFlow<List<Member>> = group.map { group -> group?.id }
        .distinctUntilChanged()
        .flatMapLatest { groupId ->
            if (groupId == null) {
                flowOf(emptyList())
            } else {
                memberService.observeMembers(groupId)
            }
        }
        .flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    override fun observeMembers(): Flow<List<Member>> = members

    override suspend fun requireUser(): User = user.value ?: throw NotAuthenticatedException()

    override suspend fun requireMembership(): Membership = membership.value ?: throw NotAuthenticatedException()

    override suspend fun requireGroup(): Group = group.value ?: throw NotAuthenticatedException()
}