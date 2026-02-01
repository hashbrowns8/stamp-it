package it.stamp.domain.service

import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface CurrentUserContextService {
    fun observeUser(): Flow<User?>
    fun observeMembership(): Flow<Membership?>
    fun observeGroup(): Flow<Group?>
    fun observeMembers(): Flow<List<Member>>
    suspend fun requireUser(): User
    suspend fun requireMembership(): Membership
    suspend fun requireGroup(): Group
}