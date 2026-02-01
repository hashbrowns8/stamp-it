package it.stamp.membership.core

import it.stamp.model.membership.Member

sealed interface MembershipUiAction
data object OnBackClick : MembershipUiAction
data class OnMemberMenuClick(val member: Member) : MembershipUiAction

typealias OnUiAction = (MembershipUiAction) -> Unit