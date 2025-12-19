package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    val members: Flow<List<Member>>

    suspend fun getGroupMembers(groupId: GroupId): List<Member>
}