package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow

interface MemberService {
    fun observeMembersByUser(userId: UserId): Flow<List<Member>>

    suspend fun getMembersByGroup(groupId: GroupId): List<Member>
}