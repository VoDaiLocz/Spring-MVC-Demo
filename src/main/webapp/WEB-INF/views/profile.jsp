<%@ page import="com.demo.socialmedia.model.Follow" %>
<%@ page import="com.demo.socialmedia.model.Post" %>
<%@ page import="com.demo.socialmedia.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%!
private String h(String s) {
    return org.springframework.web.util.HtmlUtils.htmlEscape(s == null ? "" : s);
}

private String initial(String s) {
    String value = s == null || s.isBlank() ? "U" : s.trim().substring(0, 1).toUpperCase();
    return h(value);
}

private String postText(Post post) {
    String title = post.getTitle() == null ? "" : post.getTitle().trim();
    String body = post.getBody() == null ? "" : post.getBody().trim();
    String text = body.length() > 0 && title.length() > 0 && !body.equals(title)
            ? title + "\n" + body
            : (body.length() > 0 ? body : title);
    return h(text);
}
%>
<%
String ctx = request.getContextPath();
User user = (User) request.getAttribute("user");
List<Post> posts = (List<Post>) request.getAttribute("posts");
List<Follow> followingUsers = (List<Follow>) request.getAttribute("followingUsers");
List<Follow> followerUsers = (List<Follow>) request.getAttribute("followerUsers");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ - SocialApp</title>
    <link rel="stylesheet" href="<%= ctx %>/static/css/style.css">
</head>
<body class="bg-main">
    <div class="x-app">
        <aside class="x-left">
            <div class="x-sticky">
                <a href="<%= ctx %>/" class="x-logo" aria-label="Trang chủ">Y</a>
                <nav class="x-nav">
                    <a href="<%= ctx %>/" class="x-nav-link">
                        <span class="x-nav-icon" aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <path d="M3 10.5l9-7 9 7"></path>
                                <path d="M5 9.5V20h14V9.5"></path>
                            </svg>
                        </span>
                        <span class="x-nav-text">Trang chủ</span>
                    </a>
                    <a href="<%= ctx %>/profile" class="x-nav-link active">
                        <span class="x-nav-icon" aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <circle cx="12" cy="8" r="4"></circle>
                                <path d="M4 21a8 8 0 0 1 16 0"></path>
                            </svg>
                        </span>
                        <span class="x-nav-text">Hồ sơ</span>
                    </a>
                </nav>

                <a href="<%= ctx %>/" class="btn btn-fill x-post-pill">Đăng</a>

                <div class="x-userbox">
                    <div class="x-user-avatar"><%= initial(user.getUsername()) %></div>
                    <div class="x-user-meta">
                        <div class="x-user-name"><%= h(user.getUsername()) %></div>
                        <div class="x-user-handle">@<%= h(user.getUsername().toLowerCase()) %></div>
                    </div>
                    <a class="x-logout-inline" href="<%= ctx %>/logout">Thoát</a>
                </div>
            </div>
        </aside>

        <main class="x-center">
            <div class="x-header x-header-title">Hồ sơ</div>

            <section class="x-section profile-top">
                <div class="profile-avatar-xl"><%= initial(user.getUsername()) %></div>
                <h1 class="profile-username"><%= h(user.getUsername()) %></h1>
                <p class="profile-meta">@<%= h(user.getUsername().toLowerCase()) %> · Tham gia <%= user.getCreatedAt() %></p>

                <div class="stats-row">
                    <div class="stat-group">
                        <div class="stat-number"><%= posts.size() %></div>
                        <div class="stat-label">Bài viết</div>
                    </div>
                    <div class="stat-group">
                        <div class="stat-number"><%= followerUsers.size() %></div>
                        <div class="stat-label">Followers</div>
                    </div>
                    <div class="stat-group">
                        <div class="stat-number"><%= followingUsers.size() %></div>
                        <div class="stat-label">Following</div>
                    </div>
                </div>
            </section>

            <section class="x-section profile-posts-heading">
                <h3 class="profile-posts-title">Bài viết của bạn</h3>
            </section>

            <section id="postsList">
                <% if (posts.isEmpty()) { %>
                <div class="x-section-muted">Chưa có bài viết nào.</div>
                <% } %>
                <% for (Post post : posts) { %>
                <article class="profile-post-card">
                    <div class="profile-post-meta"><%= post.getCreatedAt() %></div>
                    <div class="profile-post-text"><%= postText(post) %></div>
                </article>
                <% } %>
            </section>
        </main>

        <aside class="x-right">
            <div class="x-sticky">
                <div class="x-widget" style="margin-top:0; margin-bottom:12px;">
                    <h3 class="x-side-title">Đang theo dõi</h3>
                    <div id="followingList">
                        <% if (followingUsers.isEmpty()) { %>
                        <p class="x-empty-hint">Chưa theo dõi ai.</p>
                        <% } %>
                        <% for (Follow follow : followingUsers) { %>
                        <div class="user-row">
                            <div class="user-info-sm">
                                <div class="avatar-sm"><%= initial(follow.getUsername()) %></div>
                                <span style="font-size:14px;"><%= h(follow.getUsername()) %></span>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>

                <div class="x-widget">
                    <h3 class="x-side-title">Người theo dõi</h3>
                    <div id="followersList">
                        <% if (followerUsers.isEmpty()) { %>
                        <p class="x-empty-hint">Chưa có người theo dõi.</p>
                        <% } %>
                        <% for (Follow follow : followerUsers) { %>
                        <div class="user-row">
                            <div class="user-info-sm">
                                <div class="avatar-sm"><%= initial(follow.getUsername()) %></div>
                                <span style="font-size:14px;"><%= h(follow.getUsername()) %></span>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </aside>
    </div>
</body>
</html>
