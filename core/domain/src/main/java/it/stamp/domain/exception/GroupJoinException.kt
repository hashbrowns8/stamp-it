package it.stamp.domain.exception

import it.stamp.model.ids.GroupId

sealed class GroupJoinException(override val message: String? = null) : RuntimeException(message) {

    class InvalidInviteCode : GroupJoinException()

    class RequiresDataLossConsent(val targetGroupId: GroupId) : GroupJoinException()

    class AlreadyMember : GroupJoinException()
}
