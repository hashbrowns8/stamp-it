package it.stamp.domain.service

import it.stamp.model.membership.Group
import it.stamp.model.user.User

interface GroupTransferService {

    suspend fun transfer(
        user: User,
        leavingGroup: Group,
        joiningGroup: Group,
    ): Group
}