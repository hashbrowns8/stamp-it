package it.stamp.model.user

import kotlin.uuid.Uuid

@JvmInline
value class DisplayName(val value: String) {
    companion object {
        private val adjectives = listOf("행복한", "즐거운", "활발한", "따뜻한", "밝은")

        private val nouns = listOf("사자", "호랑이", "곰", "토끼", "고양이")

        operator fun invoke(): DisplayName = buildString {
            append(adjectives.random())
            append(' ')
            append(nouns.random())
            append('-')
            append((100..999).random())
            Uuid.random().toString()
                .take(4)
                .let(::append)
        }.let(::DisplayName)
    }
}