<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%! private String h(String s) { return org.springframework.web.util.HtmlUtils.htmlEscape(s == null ? "" : s); } %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - SocialApp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body class="bg-main">
    <div class="auth-shell">
        <div class="auth-card card">
            <h1 class="auth-brand">SocialApp</h1>
            <h2 class="auth-title">Tạo tài khoản</h2>
            <p class="auth-subtitle">Tham gia và kết nối ngay</p>

            <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateRegisterForm();">
                <div class="form-group">
                    <label class="form-label" for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" class="form-input" placeholder="username" autocomplete="username" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-input" placeholder="••••••••" autocomplete="new-password" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="confirm">Xác nhận mật khẩu</label>
                    <input type="password" id="confirm" class="form-input" placeholder="••••••••" autocomplete="new-password" required>
                </div>

                <button type="submit" class="btn btn-fill" style="width:100%;">Đăng ký</button>
            </form>

            <% if (request.getAttribute("error") != null) { %>
            <div id="toast-container">
                <div class="toast toast-error"><%= h(String.valueOf(request.getAttribute("error"))) %></div>
            </div>
            <% } %>

            <p class="auth-footer">
                Đã có tài khoản?
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
            </p>
        </div>
    </div>

    <script>
        function validateRegisterForm() {
            const password = document.getElementById('password').value;
            const confirm = document.getElementById('confirm').value;
            if (password !== confirm) {
                alert('Mật khẩu xác nhận không khớp');
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
