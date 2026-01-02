package it.stamp.model.membership

@JvmInline
value class InviteCode(val value: String) {
    companion object {
        val Empty = InviteCode(String())
    }
}