package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Post;
import com.demo.socialmedia.model.Follow;
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

class UserControllerSessionTest {

    private final UserService userService = mock(UserService.class);
    private final PostService postService = mock(PostService.class);
    private final FollowService followService = mock(FollowService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController();
        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "postService", postService);
        ReflectionTestUtils.setField(controller, "followService", followService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    void profileRedirectsToLoginWhenSessionMissing() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void profileLoadsCurrentUserAndPostsFromSession() throws Exception {
        User user = new User();
        user.setId(7);
        user.setUsername("bob");

        Post post = new Post();
        post.setId(10);
        post.setTitle("Hello");
        Follow following = new Follow();
        following.setFollowedUserId(8);
        following.setUsername("alice");
        Follow follower = new Follow();
        follower.setFollowingUserId(9);
        follower.setUsername("charlie");

        when(userService.getUserById(7)).thenReturn(user);
        when(postService.getPostsByUserId(7)).thenReturn(List.of(post));
        when(followService.getFollowing(7)).thenReturn(List.of(following));
        when(followService.getFollowers(7)).thenReturn(List.of(follower));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAuth.CURRENT_USER_ID, 7);

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("posts", List.of(post)))
                .andExpect(model().attribute("followingUsers", List.of(following)))
                .andExpect(model().attribute("followerUsers", List.of(follower)));
    }

    private InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
