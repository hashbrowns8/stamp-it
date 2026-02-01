package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow

interface MemberService {

    fun observeMembers(groupId: GroupId): Flow<List<Member>>

    suspend fun getMembers(groupId: GroupId): List<Member>
}