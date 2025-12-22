package it.stamp.domain.generator

import it.stamp.model.membership.InviteCode
import kotlin.uuid.Uuid

object InviteCodeGenerator {
    fun generate() = Uuid.random().toString()
        .replace("-", "")
        .take(8)
        .uppercase()
        .let(::InviteCode)
}