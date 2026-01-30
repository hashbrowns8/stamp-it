package it.stamp.domain.exception

sealed class MembershipException(override val message: String? = null) : RuntimeException(message)

class MembershipNotFoundException : MembershipException()

class MemberNotFoundException : MembershipException()

class MemberAlreadyRemovedException : MembershipException()