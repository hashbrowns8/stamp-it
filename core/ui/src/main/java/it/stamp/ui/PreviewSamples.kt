package it.stamp.ui

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import it.stamp.model.membership.Member
import it.stamp.model.membership.Role
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.uuid.Uuid

data object PreviewSamples {
    val group = Group(
        id = GroupId(Uuid.random().toString()),
        name = "👦🏻👧🏻🧑🏻👩🏻👨🏻",
        inviteCode = InviteCode(),
        createdAt = Clock.System.now(),
    )

    val me = User(
        id = UserId("1"),
        displayName = DisplayName("즐거운 호랑이"),
        avatar = Avatar.CHARACTER_1,
    )

    val members = listOf(
        Member(
            id = UserId("1"),
            groupId = group.id,
            displayName = DisplayName("즐거운 호랑이"),
            avatar = Avatar.CHARACTER_1,
            role = Role.LEADER,
            joinedAt = Clock.System.now(),
        ),
        Member(
            id = UserId("2"),
            groupId = group.id,
            displayName = DisplayName("엄마"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        ),
        Member(
            id = UserId("3"),
            groupId = group.id,
            displayName = DisplayName("아빠"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        ),
        Member(
            id = UserId("4"),
            groupId = group.id,
            displayName = DisplayName("행복한 호랑이"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        )
    )

    val myMissions = listOf(
        Mission(
            id = MissionId("1"),
            groupId = group.id,
            category = MissionCategory.COMMUNICATION,
            title = "할머니께 연락하기",
            assignee = me.id,
            assigner = UserId("2"),
            dueDate = LocalDate(2025, 12, 22),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("2"),
            groupId = group.id,
            category = MissionCategory.CHORE,
            title = "이불 빨고 말리기",
            assignee = me.id,
            assigner = UserId("4"),
            dueDate = Clock.System
                .todayIn(TimeZone.currentSystemDefault())
                .plus(5, DateTimeUnit.DAY),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("3"),
            groupId = group.id,
            category = MissionCategory.HEALTH,
            title = "건강한 수면 환경 함께 조성하기",
            assignee = me.id,
            assigner = UserId("4"),
            dueDate = Clock.System
                .todayIn(TimeZone.currentSystemDefault())
                .plus(7, DateTimeUnit.DAY),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
    )

    val memberMissions = listOf(
        MemberMissionUiModel(
            id = MissionId("1"),
            category = MissionCategory.COMMUNICATION,
            title = "할머니께 연락하기",
            assigneeId = UserId("2"),
            assigneeDisplayName = "엄마",
            dueDate = LocalDate(2026, 1, 20),
            daysAgo = "오늘",
            status = MissionStatus.ASSIGNED,
            isOverdue = false,
            isDone = false,
        ),
        MemberMissionUiModel(
            id = MissionId("2"),
            category = MissionCategory.CHORE,
            title = "방 청소하기",
            assigneeId = UserId("3"),
            assigneeDisplayName = "아빠",
            dueDate = LocalDate(2026, 1, 21),
            daysAgo = "내일",
            status = MissionStatus.ASSIGNED,
            isOverdue = false,
            isDone = false,
        ),
        MemberMissionUiModel(
            id = MissionId("3"),
            category = MissionCategory.CHORE,
            title = "화장실 청소하기",
            assigneeId = UserId("4"),
            assigneeDisplayName = "행복한 호랑이",
            dueDate = LocalDate(2026, 1, 23),
            daysAgo = "3일 전",
            status = MissionStatus.ASSIGNED,
            isOverdue = false,
            isDone = false,
        )
    )
}