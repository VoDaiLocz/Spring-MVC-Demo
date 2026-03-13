# Social Media App - Spring MVC Demo

Ứng dụng mạng xã hội demo sử dụng **Spring MVC truyền thống** với cấu hình XML, MySQL qua **JdbcTemplate** và giao diện **JSP server-rendered**.

## Công nghệ sử dụng
- **Backend:** Spring MVC 5.3, Java 17/21, Maven WAR
- **Database:** MySQL 8, Spring JDBC / JdbcTemplate
- **Frontend:** JSP server-rendered + CSS thuần
- **Server:** Apache Tomcat 9

## Tính năng chính
1. **Quản lý người dùng**
- Đăng ký tài khoản mới
- Đăng nhập bằng **server-side session**
- Đăng xuất bằng server-side session

2. **Quản lý bài viết**
- Tạo bài viết mới
- Xem bảng tin từ tài khoản của mình và người đang theo dõi
- Xem hồ sơ cá nhân

3. **Follow / Unfollow**
- Theo dõi và hủy theo dõi tài khoản khác
- Xem hồ sơ với số bài viết, following, followers

## Cấu trúc dự án
```text
social-media-app/
├── database/schema.sql
├── docs/
├── pom.xml
├── REQUIREMENTS.md
└── src/main/
    ├── java/com/demo/socialmedia/
    │   ├── controller/
    │   ├── dao/
    │   ├── model/
    │   ├── service/
    │   └── util/
    └── webapp/
        ├── WEB-INF/
        │   ├── web.xml
        │   └── spring/dispatcher-servlet.xml
        │   └── views/
        └── static/
            └── css/style.css
```

## Yêu cầu môi trường
- JDK 17 hoặc JDK 21
- Apache Maven 3.8+
- MySQL 8+
- Apache Tomcat 9+

## Cài đặt và chạy

### 1. Khởi tạo cơ sở dữ liệu
File `database/schema.sql` là script setup đầy đủ. Script sẽ reset lại database `social_media_db` và nạp dữ liệu mẫu.

Ví dụ chạy bằng MySQL CLI:

```bash
mysql -u root -p < database/schema.sql
```

### 2. Cấu hình kết nối MySQL
Kiểm tra file `src/main/webapp/WEB-INF/spring/dispatcher-servlet.xml` và cập nhật lại:
- `url`
- `username`
- `password`

Mặc định repo đang dùng:
- database: `social_media_db`
- user: `root`
- password: rỗng

### 3. Build project
```bash
mvn clean test
mvn clean package
```

### 4. Deploy WAR lên Tomcat
Copy file `target/social-media-app.war` vào thư mục `webapps` của Tomcat rồi khởi động Tomcat.

### 5. Truy cập ứng dụng
Sau khi deploy xong:

```text
http://localhost:8080/social-media-app/
```

Ứng dụng hiện dùng DispatcherServlet tại `/` và render JSP trực tiếp từ `WEB-INF/views`.
