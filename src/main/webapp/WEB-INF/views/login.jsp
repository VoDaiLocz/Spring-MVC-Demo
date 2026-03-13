<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%! private String h(String s) { return org.springframework.web.util.HtmlUtils.htmlEscape(s == null ? "" : s); } %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - SocialApp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body class="bg-main">
    <div class="auth-shell">
        <div class="auth-card card">
            <h1 class="auth-brand">SocialApp</h1>
            <h2 class="auth-title">Đăng nhập</h2>
            <p class="auth-subtitle">Tiếp tục với tài khoản của bạn</p>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label class="form-label" for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" class="form-input" placeholder="username" autocomplete="username" required>
                </div>
                <div class="form-group">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-input" placeholder="••••••••" autocomplete="current-password" required>
                </div>
                <button type="submit" class="btn btn-fill" style="width:100%;">Đăng nhập</button>
            </form>

            <% if (request.getAttribute("error") != null) { %>
            <div id="toast-container">
                <div class="toast toast-error"><%= h(String.valueOf(request.getAttribute("error"))) %></div>
            </div>
            <% } %>

            <p class="auth-footer">
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
            </p>
        </div>
    </div>
</body>
</html>
