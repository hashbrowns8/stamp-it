package it.stamp.model.user

enum class Avatar {
    CHARACTER_1,
    CHARACTER_2,
    CHARACTER_3,
    CHARACTER_4,
    CHARACTER_5,
    CHARACTER_6,
    CHARACTER_7,
    CHARACTER_8;

    companion object {
        fun valueOf(value: Int): Avatar {
            return entries[value - 1]
        }
    }
}