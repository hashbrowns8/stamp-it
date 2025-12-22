package it.stamp.domain.generator

import java.security.MessageDigest

object DisplayNameGenerator {
    private val adjectives = listOf(
        "행복한",
        "즐거운",
        "활발한",
        "따뜻한",
        "밝은",
    )

    private val nouns = listOf(
        "사자",
        "호랑이",
        "곰",
        "토끼",
        "고양이",
    )

    private val messageDigest = MessageDigest.getInstance("SHA-256")

    private fun String.sha256(): String {
        val input = toByteArray()
        val bytes = messageDigest.digest(input)
        return bytes.joinToString(separator = "")  { "%02x".format(it) }
    }

    fun generate(uid: String): String {
        val randomAdjective = adjectives.random()
        val randomNoun = nouns.random()
        val randomNumber = (100..999).random()
        val hashedId = uid.sha256().takeLast(4)
        return "$randomAdjective $randomNoun-$randomNumber$hashedId"
    }
}