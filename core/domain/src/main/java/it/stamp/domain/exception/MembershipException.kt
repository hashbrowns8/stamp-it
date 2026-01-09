package it.stamp.domain.exception

sealed class MembershipException : Exception()

class MembershipNotFoundException : MembershipException()