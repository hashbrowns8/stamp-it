package it.stamp.domain.exception

sealed class MembershipException(override val message: String? = null) : RuntimeException(message)

class MembershipNotFoundException : MembershipException()

sealed class LeadershipException(override val message: String? = null) : MembershipException(message) {
    class Unauthorized : LeadershipException()
}

sealed class MemberRemovalException(override val message: String? = null) : MembershipException(message) {
    class MemberNotFound : MemberRemovalException()
}