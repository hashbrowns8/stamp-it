package it.stamp.domain.exception

sealed class AccountDeletionException(override val message: String? = null) : RuntimeException(message) {
    class LeadershipTransferRequired : AccountDeletionException()

    class Unauthorized : AccountDeletionException()
}