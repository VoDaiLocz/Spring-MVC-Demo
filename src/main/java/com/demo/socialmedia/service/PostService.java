package com.demo.socialmedia.service;

import com.demo.socialmedia.dao.PostDAO;
import com.demo.socialmedia.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service xử lý nghiệp vụ liên quan đến Post.
 */
@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    // Lấy tất cả bài viết
    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    // Lấy bài viết theo ID
    public Post getPostById(int id) {
        return postDAO.findById(id);
    }

    // Lấy bài viết của 1 user
    public List<Post> getPostsByUserId(int userId) {
        return postDAO.findByUserId(userId);
    }

    // Lấy newsfeed (bài viết của mình + người mình follow)
    public List<Post> getFeedByUserId(int userId) {
        return postDAO.findFeedByUserId(userId);
    }

    // Tạo bài viết mới
    public boolean createPost(Post post) {
        return postDAO.insert(post) > 0;
    }

    // Cập nhật bài viết
    public boolean updatePost(Post post) {
        return postDAO.update(post) > 0;
    }

    // Xóa bài viết
    public boolean deletePost(int id, int userId) {
        return postDAO.delete(id, userId) > 0;
    }
}
