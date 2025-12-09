package it.stamp.model.ids

@JvmInline
value class UserId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class GroupId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class MissionId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class StampId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
value class NotificationId(val value: String) {
    init {
        require(value.isNotBlank())
    }
}