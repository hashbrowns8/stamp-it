package it.stamp.domain.service

import it.stamp.model.membership.Membership

interface MembershipService {

    suspend fun transferLeadership(from: Membership, to: Membership)

    suspend fun removeMember(membership: Membership)
}