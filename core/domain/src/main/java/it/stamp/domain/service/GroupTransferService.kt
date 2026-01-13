package it.stamp.domain.service

import it.stamp.model.membership.Group
import it.stamp.model.user.User

interface GroupTransferService {
    suspend fun transferGroup(
        user: User,
        leavingGroup: Group,
        joiningGroup: Group,
    ): Group
}