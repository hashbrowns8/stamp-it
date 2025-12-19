package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow

interface MemberService {
    val members: Flow<List<Member>>

    suspend fun getGroupMembers(groupId: GroupId): List<Member>
}