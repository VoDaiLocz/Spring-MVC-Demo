package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Follow;
import com.demo.socialmedia.model.Post;
import com.demo.socialmedia.model.User;
import com.demo.socialmedia.service.FollowService;
import com.demo.socialmedia.service.PostService;
import com.demo.socialmedia.service.UserService;
import com.demo.socialmedia.util.SessionAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HomeControllerTest {

    private final PostService postService = mock(PostService.class);
    private final FollowService followService = mock(FollowService.class);
    private final UserService userService = mock(UserService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        HomeController controller = new HomeController();
        ReflectionTestUtils.setField(controller, "postService", postService);
        ReflectionTestUtils.setField(controller, "followService", followService);
        ReflectionTestUtils.setField(controller, "userService", userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    void homeRedirectsToLoginWhenSessionMissing() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void homeLoadsFeedAndFollowLists() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAuth.CURRENT_USER_ID, 2);
        User currentUser = new User();
        currentUser.setId(2);
        currentUser.setUsername("alice");

        when(userService.getUserById(2)).thenReturn(currentUser);
        when(postService.getFeedByUserId(2)).thenReturn(List.of(new Post()));
        when(followService.getFollowing(2)).thenReturn(List.of(new Follow()));
        when(followService.getFollowers(2)).thenReturn(List.of(new Follow()));
        when(followService.getSuggestedUsers(2)).thenReturn(List.of());

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("followingUsers"))
                .andExpect(model().attributeExists("followerUsers"))
                .andExpect(model().attributeExists("suggestedUsers"));
    }

    private InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
