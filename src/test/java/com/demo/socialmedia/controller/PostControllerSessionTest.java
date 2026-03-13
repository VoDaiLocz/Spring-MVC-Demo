package com.demo.socialmedia.controller;

import com.demo.socialmedia.model.Post;
import com.demo.socialmedia.service.PostService;
import com.demo.socialmedia.util.SessionAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerSessionTest {

    private final PostService postService = mock(PostService.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        PostController controller = new PostController();
        ReflectionTestUtils.setField(controller, "postService", postService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPostRedirectsToLoginWhenSessionMissing() throws Exception {
        mockMvc.perform(post("/post")
                        .param("title", "Hello")
                        .param("body", "Body"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void createPostUsesUserIdFromSession() throws Exception {
        when(postService.createPost(org.mockito.ArgumentMatchers.any(Post.class))).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAuth.CURRENT_USER_ID, 11);

        mockMvc.perform(post("/post")
                        .session(session)
                        .param("title", "Hello")
                        .param("body", "Body")
                        .param("userId", "999")
                        .param("status", "PUBLISHED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postService).createPost(captor.capture());
        assertEquals(11, captor.getValue().getUserId());
        assertEquals("Hello", captor.getValue().getTitle());
        assertEquals("Body", captor.getValue().getBody());
    }
}
