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
User currentUser = (User) request.getAttribute("currentUser");
List<User> suggestedUsers = (List<User>) request.getAttribute("suggestedUsers");
List<Post> posts = (List<Post>) request.getAttribute("posts");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng tin - SocialApp</title>
    <link rel="stylesheet" href="<%= ctx %>/static/css/style.css">
</head>
<body class="bg-main">
    <div class="x-app">
        <aside class="x-left">
            <div class="x-sticky">
                <a href="<%= ctx %>/" class="x-logo" aria-label="Trang chủ">Y</a>

                <nav class="x-nav">
                    <a href="<%= ctx %>/" class="x-nav-link active">
                        <span class="x-nav-icon" aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <path d="M3 10.5l9-7 9 7"></path>
                                <path d="M5 9.5V20h14V9.5"></path>
                            </svg>
                        </span>
                        <span class="x-nav-text">Trang chủ</span>
                    </a>
                    <a href="<%= ctx %>/profile" class="x-nav-link">
                        <span class="x-nav-icon" aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <circle cx="12" cy="8" r="4"></circle>
                                <path d="M4 21a8 8 0 0 1 16 0"></path>
                            </svg>
                        </span>
                        <span class="x-nav-text">Hồ sơ</span>
                    </a>
                </nav>

                <a href="#composerForm" class="btn btn-fill x-post-pill">Đăng</a>

                <div class="x-userbox">
                    <div class="x-user-avatar"><%= initial(currentUser.getUsername()) %></div>
                    <div class="x-user-meta">
                        <div class="x-user-name"><%= h(currentUser.getUsername()) %></div>
                        <div class="x-user-handle">@<%= h(currentUser.getUsername().toLowerCase()) %></div>
                    </div>
                    <a class="x-logout-inline" href="<%= ctx %>/logout">Thoát</a>
                </div>
            </div>
        </aside>

        <main class="x-center">
            <div class="x-header x-header-title">Trang chủ</div>

            <section class="x-section composer-section">
                <div class="composer-row">
                    <div class="avatar-sm composer-avatar"><%= initial(currentUser.getUsername()) %></div>
                    <div class="composer-main">
                        <form action="<%= ctx %>/post" method="post" id="composerForm">
                            <input type="text" id="postTitle" name="title" class="form-input composer-title" placeholder="Tiêu đề">
                            <textarea id="postBody" name="body" class="composer-body composer-body-main" placeholder="Chuyện gì đang xảy ra?"></textarea>
                            <div class="composer-actions">
                                <div class="composer-tools" aria-hidden="true">
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <rect x="3" y="4" width="18" height="16" rx="2"></rect>
                                            <circle cx="8.5" cy="9" r="1.5"></circle>
                                            <path d="M21 15l-5-5-8 8"></path>
                                        </svg>
                                    </span>
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <path d="M4 7h14a2 2 0 0 1 2 2v8H4z"></path>
                                            <path d="M4 11l6-3v11l-6-3z"></path>
                                        </svg>
                                    </span>
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <circle cx="12" cy="12" r="9"></circle>
                                            <path d="M8 14s1.5 2 4 2 4-2 4-2"></path>
                                            <path d="M9 10h.01M15 10h.01"></path>
                                        </svg>
                                    </span>
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <path d="M12 21s6-6.3 6-11a6 6 0 1 0-12 0c0 4.7 6 11 6 11z"></path>
                                            <circle cx="12" cy="10" r="2.5"></circle>
                                        </svg>
                                    </span>
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <path d="M4 12h16"></path>
                                            <path d="M12 4v16"></path>
                                            <circle cx="12" cy="12" r="9"></circle>
                                        </svg>
                                    </span>
                                    <span class="composer-tool-icon">
                                        <svg viewBox="0 0 24 24">
                                            <path d="M4 6h16"></path>
                                            <path d="M4 12h10"></path>
                                            <path d="M4 18h13"></path>
                                            <circle cx="18" cy="12" r="2"></circle>
                                        </svg>
                                    </span>
                                </div>
                                <button class="btn btn-fill composer-submit" id="btnPublish" type="submit">Đăng</button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>

            <section id="feedList">
                <% if (posts.isEmpty()) { %>
                <div class="x-section-muted">Chưa có bài viết trong bảng tin.</div>
                <% } %>
                <% for (Post post : posts) { %>
                <article class="post-card">
                    <div class="post-header">
                        <div class="post-author">
                            <div class="avatar-sm"><%= initial(post.getAuthorName() != null ? post.getAuthorName() : ("U" + post.getUserId())) %></div>
                            <div class="author-headline">
                                <span class="author-name"><%= h(post.getAuthorName() != null ? post.getAuthorName() : ("User " + post.getUserId())) %></span>
                                <span class="post-meta">· <%= post.getCreatedAt() %></span>
                            </div>
                        </div>
                    </div>
                    <div class="post-text"><%= postText(post) %></div>
                </article>
                <% } %>
            </section>
        </main>

        <aside class="x-right">
            <div class="x-sticky">
                <div class="x-widget" style="margin-top:0;">
                    <h3 class="x-side-title">Gợi ý theo dõi</h3>
                    <div id="recommendedUsers">
                        <% if (suggestedUsers.isEmpty()) { %>
                        <p class="x-empty-hint">Không còn gợi ý phù hợp.</p>
                        <% } %>
                        <% for (User user : suggestedUsers) { %>
                        <div class="user-row">
                            <div class="user-info-sm">
                                <div class="avatar-sm"><%= initial(user.getUsername()) %></div>
                                <div class="user-meta-mini">
                                    <div class="user-info-name"><%= h(user.getUsername()) %></div>
                                    <div class="user-info-handle">@<%= h(user.getUsername().toLowerCase()) %></div>
                                </div>
                            </div>
                            <form action="<%= ctx %>/follow/add" method="post">
                                <input type="hidden" name="followedUserId" value="<%= user.getId() %>">
                                <button class="btn btn-fill x-follow-btn" type="submit">Theo dõi</button>
                            </form>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </aside>
    </div>

    <script>
        const postTitleEl = document.getElementById('postTitle');
        const postBodyEl = document.getElementById('postBody');
        const btnPublishEl = document.getElementById('btnPublish');

        function syncComposerState() {
            btnPublishEl.disabled =
                postTitleEl.value.trim().length === 0 ||
                postBodyEl.value.trim().length === 0;
        }

        postTitleEl.addEventListener('input', syncComposerState);
        postBodyEl.addEventListener('input', syncComposerState);
        syncComposerState();
    </script>
</body>
</html>
