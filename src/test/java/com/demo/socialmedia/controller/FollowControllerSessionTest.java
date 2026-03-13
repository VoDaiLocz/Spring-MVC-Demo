package com.demo.socialmedia.controller;

import com.demo.socialmedia.service.FollowService;
import com.demo.socialmedia.util.SessionAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerSessionTest {

    private final FollowService followService = mock(FollowService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FollowController controller = new FollowController();
        ReflectionTestUtils.setField(controller, "followService", followService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void followRedirectsToLoginWhenSessionMissing() throws Exception {
        mockMvc.perform(post("/follow/add").param("followedUserId", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void followUsesUserIdFromSession() throws Exception {
        when(followService.follow(4, 9)).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAuth.CURRENT_USER_ID, 4);

        mockMvc.perform(post("/follow/add")
                        .session(session)
                        .param("followingUserId", "777")
                        .param("followedUserId", "9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(followService).follow(4, 9);
    }

    @Test
    void unfollowUsesUserIdFromSession() throws Exception {
        when(followService.unfollow(4, 9)).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAuth.CURRENT_USER_ID, 4);

        mockMvc.perform(post("/follow/remove")
                        .session(session)
                        .param("followingUserId", "777")
                        .param("followedUserId", "9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(followService).unfollow(4, 9);
    }
}
