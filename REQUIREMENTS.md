# ĐỀ BÀI: XÂY DỰNG ỨNG DỤNG MẠNG XÃ HỘI (SPRING MVC DEMO)

## 🎯 Mục tiêu dự án
Xây dựng một ứng dụng mạng xã hội (Social Media) đơn giản để làm demo học tập, sử dụng framework **Spring MVC truyền thống** kết hợp với **REST API** và giao diện hiện đại.

## 🛠️ Yêu cầu kỹ thuật
*   **Backend:** Spring MVC 5.x (Cấu hình bằng XML: `web.xml`, `dispatcher-servlet.xml`).
*   **Ngôn ngữ:** Java (tương thích JDK 17/21).
*   **Quản lý thư viện:** Maven (`pom.xml`).
*   **Cấu trúc dự án:** Model - DAO - Service - Controller.
*   **Cơ sở dữ liệu:** MySQL (Sử dụng Spring JDBC / JdbcTemplate để quản lý kết nối).
*   **Frontend:** HTML/JS/CSS thuần (Giao diện hiện đại, chuyên nghiệp, gọi API lấy dữ liệu JSON).

## ✨ Các tính năng chính
1.  **Quản lý người dùng:**
    *   Đăng ký tài khoản mới.
    *   Đăng nhập hệ thống (Xử lý Session).
2.  **Quản lý bài viết (CRUD Posts):**
    *   Đăng bài viết mới (Tiêu đề, nội dung).
    *   Xem bảng tin (Feed) gồm các bài viết từ người mình theo dõi.
    *   Chỉnh sửa và Xóa bài viết cá nhân.
3.  **Hệ thống kết nối (Follow/Unfollow):**
    *   Danh sách người dùng trong hệ thống.
    *   Theo dõi và Hủy theo dõi người dùng khác.
    *   Trang cá nhân (Profile) hiển thị các chỉ số: Bài đăng, Following, Followers.

## 📄 Sản phẩm bàn giao
*   **Mã nguồn:** Full folder dự án đã được cấu hình chạy trên Tomcat.
*   **Cơ sở dữ liệu:** File `schema.sql` để khởi tạo bảng và dữ liệu mẫu.
*   **Báo cáo (Word):** Nội dung chi tiết về kiến trúc, luồng xử lý và giải thích code.
*   **Trình bày (PPT):** Slide tóm tắt chuyên nghiệp dùng để thuyết trình demo.

---
*Ghi chú: Ưu tiên mã nguồn sạch, dễ hiểu cho mục đích học tập nhưng giao diện phải bắt mắt (Premium / Minimalist style).*
