<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
</head>
<body>
    <h2>Đăng nhập</h2>

    <p style="color:red">${loi}</p>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div>
            Tên đăng nhập: <input type="text" name="tenDangNhap" required>
        </div>
        <div>
            Mật khẩu: <input type="password" name="matKhau" required>
        </div>
        <button type="submit">Đăng nhập</button>
    </form>
</body>
</html>