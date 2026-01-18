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

    init {
        require(value.length == 8)
        require(value.all(Char::isLetterOrDigit))
    }
}