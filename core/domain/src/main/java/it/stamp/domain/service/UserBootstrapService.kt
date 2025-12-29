package it.stamp.domain.service

import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.user.User

interface UserBootstrapService {
    suspend fun bootstrap(user: User, group: Group, membership: Membership): Result<Unit>
}