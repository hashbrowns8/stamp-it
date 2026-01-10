package it.stamp.domain.exception

sealed class MembershipException(override val message: String? = null) : RuntimeException(message)

class MemberNotFoundException() : MembershipException()