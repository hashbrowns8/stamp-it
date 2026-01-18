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
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.uuid.Uuid

data object PreviewSamples {

    val Group = Group(
        id = GroupId(Uuid.random().toString()),
        name = "👦🏻👧🏻🧑🏻👩🏻👨🏻",
        inviteCode = InviteCode(),
        createdAt = Clock.System.now(),
    )

    val Me = User(
        id = UserId("1"),
        displayName = DisplayName("즐거운 호랑이"),
        avatar = Avatar.CHARACTER_1,
    )

    val MeAsMember = Member(
        id = Me.id,
        groupId = Group.id,
        displayName = Me.displayName,
        avatar = Me.avatar,
        role = Role.LEADER,
        joinedAt = Clock.System.now(),
    )

    val Members = listOf(
        MeAsMember,
        Member(
            id = UserId("2"),
            groupId = Group.id,
            displayName = DisplayName("엄마"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        ),
        Member(
            id = UserId("3"),
            groupId = Group.id,
            displayName = DisplayName("아빠"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        ),
        Member(
            id = UserId("4"),
            groupId = Group.id,
            displayName = DisplayName("행복한 호랑이-ABCDEFG"),
            avatar = Avatar.CHARACTER_1,
            role = Role.MEMBER,
            joinedAt = Clock.System.now(),
        )
    )

    val MyMissions = listOf(
        Mission(
            id = MissionId("1"),
            groupId = Group.id,
            category = MissionCategory.COMMUNICATION,
            title = "할머니께 연락하기",
            assignee = MeAsMember.id,
            assigner = UserId("4"),
            dueDate = LocalDate(2025, 12, 22),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("2"),
            groupId = Group.id,
            category = MissionCategory.CHORE,
            title = "이불 빨고 말리기",
            assignee = MeAsMember.id,
            assigner = UserId("4"),
            dueDate = Clock.System
                .todayIn(TimeZone.currentSystemDefault())
                .plus(5, DateTimeUnit.DAY),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("3"),
            groupId = Group.id,
            category = MissionCategory.HEALTH,
            title = "건강한 수면 환경 함께 조성하기",
            assignee = MeAsMember.id,
            assigner = UserId("4"),
            dueDate = Clock.System
                .todayIn(TimeZone.currentSystemDefault())
                .plus(7, DateTimeUnit.DAY),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
    )

    val MemberMissions = listOf(
        Mission(
            id = MissionId("1"),
            groupId = Group.id,
            category = MissionCategory.COMMUNICATION,
            title = "할머니께 연락하기",
            assignee = UserId("4"),
            assigner = MeAsMember.id,
            dueDate = LocalDate(2025, 12, 22),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("2"),
            groupId = Group.id,
            category = MissionCategory.CHORE,
            title = "방 청소하기",
            assignee = UserId("4"),
            assigner = MeAsMember.id,
            dueDate = LocalDate(2025, 12, 23),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        ),
        Mission(
            id = MissionId("3"),
            groupId = Group.id,
            category = MissionCategory.CHORE,
            title = "화장실 청소하기",
            assignee = UserId("4"),
            assigner = MeAsMember.id,
            dueDate = LocalDate(2025, 12, 24),
            status = MissionStatus.ASSIGNED,
            createdAt = Clock.System.now(),
        )
    )

    val Rankings = Members.mapIndexed { index, member ->
        val rank = index + 1
        val seed = (Members.size - rank)
        val stamps = Random.nextInt(seed * 10, (seed + 1) * 10)
        LeaderboardMember(member, rank, stamps)
    }

}