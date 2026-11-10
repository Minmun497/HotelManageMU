<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Đăng ký</title></head>
<body>
    <h2>Đăng ký tài khoản khách hàng</h2>
    <p style="color:red">${loi}</p>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <div>Họ tên: <input type="text" name="hoTen" value="${param.hoTen}" required></div>
        <div>SĐT: <input type="text" name="sdt" value="${param.sdt}" required></div>
        <div>Email: <input type="email" name="email" value="${param.email}"></div>
        <div>Tên đăng nhập: <input type="text" name="tenDangNhap" value="${param.tenDangNhap}" required></div>
        <div>Mật khẩu: <input type="password" name="matKhau" required></div>
        <div>Nhập lại mật khẩu: <input type="password" name="xacNhanMatKhau" required></div>
        <button type="submit">Đăng ký</button>
    </form>
    <a href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập</a>
</body>
</html>