package it.stamp.domain.service

import it.stamp.model.membership.Group
import it.stamp.model.user.User

interface UserAndGroupProfileService {

    suspend fun update(user: User?, group: Group?)
}