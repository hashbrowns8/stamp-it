package it.stamp.data.service

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.MemberService
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class FirestoreMemberService @Inject constructor(
    authenticationService: AuthenticationService,
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
) : MemberService {

    override val members: Flow<List<Member>> = authenticationService.user
        .flatMapLatest { user ->
            if (user == null) return@flatMapLatest flowOf(emptyList())

            membershipRepository.observeUserMembership(user.id)
                .flatMapLatest { membership ->
                    membershipRepository.observeGroupMemberships(membership.groupId)
                }
                .mapLatest { memberships ->
                    memberships.mapNotNull { membership ->
                        with(membership) {
                            val user = userRepository.findUserById(membership.userId)
                                ?: return@mapNotNull null

                            Member(
                                user.id,
                                membership.groupId,
                                user.displayName,
                                user.avatar,
                                membership.role,
                                membership.joinedAt,
                            )
                        }
                    }
                }
        }
        .catch {
            emit(emptyList())  // TODO : Crashlytics
        }

    override suspend fun getGroupMembers(groupId: GroupId): List<Member> {
        val memberships = membershipRepository.getGroupMemberships(groupId)

        val members = memberships.mapNotNull { membership ->
            val user = userRepository.findUserById(membership.userId)
                ?: return@mapNotNull null

            Member(
                user.id,
                membership.groupId,
                user.displayName,
                user.avatar,
                membership.role,
                membership.joinedAt,
            )
        }

        return members
    }
}