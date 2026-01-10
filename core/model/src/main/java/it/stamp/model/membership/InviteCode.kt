package it.stamp.model.membership

import kotlin.uuid.Uuid

@JvmInline
value class InviteCode(val value: String) {

    constructor() : this(
        value = Uuid.random().toString()
            .replace("-", "")
            .take(8)
            .uppercase()
    )

    companion object {
        val Empty = InviteCode(String())
    }
}