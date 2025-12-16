package it.stamp.data.repository

import it.stamp.domain.repository.AuthenticationRepository
import it.stamp.domain.repository.MemberRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseMemberService @Inject constructor(
    authenticationRepository: AuthenticationRepository,
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
) : MemberRepository {

    override val members: Flow<List<Member>> = authenticationRepository.user
        .map { user ->
            if (user == null) return@map emptyList()

            val membership = membershipRepository.getUserMembership(user.id)

            val groupId = membership.groupId

            getGroupMembers(groupId)
        }
        .catch { // TODO : Crashlytics
            emit(emptyList())
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getGroupMembers(groupId: GroupId): List<Member> {
        membershipRepository.getGroupMemberships(groupId)
            .map { membership ->

            }
        TODO("Not yet implemented")
    }
}