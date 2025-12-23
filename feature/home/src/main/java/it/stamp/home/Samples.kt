package it.stamp.home

import it.stamp.domain.generator.InviteCodeGenerator
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Role
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus
import it.stamp.model.stamp.LeaderboardMember
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.uuid.Uuid

val sampleGroup = Group(
    id = GroupId(Uuid.random().toString()),
    name = "👦🏻👧🏻🧑🏻👩🏻👨🏻",
    inviteCode = InviteCodeGenerator.generate(),
    createdAt = Clock.System.now(),
)

val sampleMe = Member(
    id = UserId("1"),
    groupId = sampleGroup.id,
    displayName = "즐거운 호랑이",
    avatar = String(),
    role = Role.LEADER,
    joinedAt = Clock.System.now(),
)

val sampleMembers = listOf(
    sampleMe,
    Member(
        id = UserId("2"),
        groupId = sampleGroup.id,
        displayName = "엄마",
        avatar = String(),
        role = Role.MEMBER,
        joinedAt = Clock.System.now(),
    ),
    Member(
        id = UserId("3"),
        groupId = sampleGroup.id,
        displayName = "아빠",
        avatar = String(),
        role = Role.MEMBER,
        joinedAt = Clock.System.now(),
    ),
    Member(
        id = UserId("4"),
        groupId = sampleGroup.id,
        displayName = "행복한 호랑이-ABCDEFG",
        avatar = String(),
        role = Role.MEMBER,
        joinedAt = Clock.System.now(),
    )
)

val sampleMyMissions = listOf(
    Mission(
        id = MissionId("1"),
        groupId = sampleGroup.id,
        category = MissionCategory.COMMUNICATION,
        title = "할머니께 연락하기",
        assignee = sampleMe.id,
        assigner = UserId("4"),
        dueDate = LocalDate(2025, 12, 22),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    ),
    Mission(
        id = MissionId("2"),
        groupId = sampleGroup.id,
        category = MissionCategory.CHORE,
        title = "이불 빨고 말리기",
        assignee = sampleMe.id,
        assigner = UserId("4"),
        dueDate = Clock.System
            .todayIn(TimeZone.currentSystemDefault())
            .plus(5, DateTimeUnit.DAY),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    ),
    Mission(
        id = MissionId("3"),
        groupId = sampleGroup.id,
        category = MissionCategory.HEALTH,
        title = "건강한 수면 환경 함께 조성하기",
        assignee = sampleMe.id,
        assigner = UserId("4"),
        dueDate = Clock.System
            .todayIn(TimeZone.currentSystemDefault())
            .plus(7, DateTimeUnit.DAY),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    ),
)

val sampleMemberMissions = listOf(
    Mission(
        id = MissionId("1"),
        groupId = sampleGroup.id,
        category = MissionCategory.COMMUNICATION,
        title = "할머니께 연락하기",
        assignee = UserId("4"),
        assigner = sampleMe.id,
        dueDate = LocalDate(2025, 12, 22),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    ),
    Mission(
        id = MissionId("2"),
        groupId = sampleGroup.id,
        category = MissionCategory.CHORE,
        title = "방 청소하기",
        assignee = UserId("4"),
        assigner = sampleMe.id,
        dueDate = LocalDate(2025, 12, 23),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    ),
    Mission(
        id = MissionId("3"),
        groupId = sampleGroup.id,
        category = MissionCategory.CHORE,
        title = "화장실 청소하기",
        assignee = UserId("4"),
        assigner = sampleMe.id,
        dueDate = LocalDate(2025, 12, 24),
        status = MissionStatus.ASSIGNED,
        createdAt = Clock.System.now(),
    )
)

val sampleRankings = sampleMembers.mapIndexed { index, member ->
    val rank = index + 1
    val seed = (sampleMembers.size - rank)
    val stamps = Random.nextInt(seed * 10, (seed + 1) * 10)
    LeaderboardMember(member, rank, stamps)
}
