package it.stamp.mypage.core

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.StampTopAppBar
import it.stamp.designsystem.theme.White
import it.stamp.mypage.core.profile.MyProfileScreen
import it.stamp.mypage.core.stampboard.StampboardScreen
import it.stamp.mypage.core.ui.TabTitle
import kotlinx.coroutines.launch

enum class MyPageTab {
    StampBoard, Profile;
}

@Composable
internal fun MyPageScreen(
    navigateToEditProfile: () -> Unit,
    navigateToManageMembers: () -> Unit,
    navigateToInviteMember: () -> Unit,
    navigateToJoinGroup: () -> Unit,
    modifier: Modifier = Modifier,
    tabs: List<MyPageTab> = MyPageTab.entries,
    pagerState: PagerState = rememberPagerState {
        tabs.size
    },
) {
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier, color = White) {
        Column {
            StampTopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        val tab = tabs[pagerState.currentPage]

                        TabTitle(
                            selected = tab == MyPageTab.StampBoard,
                            text = stringResource(R.string.stampboard),
                        ) {
                            coroutineScope.launch {
                                tabs.indexOf(MyPageTab.StampBoard)
                                    .let { page ->
                                        pagerState.animateScrollToPage(page, animationSpec = tween(500))
                                    }
                            }
                        }

                        TabTitle(
                            selected = tab == MyPageTab.Profile,
                            text = stringResource(R.string.profile),
                        ) {
                            coroutineScope.launch {
                                tabs.indexOf(MyPageTab.Profile)
                                    .let { page ->
                                        pagerState.animateScrollToPage(page, animationSpec = tween(500))
                                    }
                            }
                        }
                    }
                },
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
            ) { page ->
                when (tabs[page]) {
                    MyPageTab.StampBoard -> {
                        StampboardScreen()
                    }
                    MyPageTab.Profile -> {
                        MyProfileScreen(
                            navigateToEditProfile,
                            navigateToManageMembers,
                            navigateToInviteMember,
                            navigateToJoinGroup,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        )
                    }
                }
            }
        }
    }
}