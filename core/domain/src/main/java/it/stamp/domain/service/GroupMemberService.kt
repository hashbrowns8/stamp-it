package it.stamp.domain.service

import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow

interface GroupMemberService {

    fun observeMyGroupMembers(): Flow<List<Member>>

    suspend fun getMyGroupMembers(): List<Member>
}