<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Khách hàng</title></head>
<body>
    <h2>${empty khach || khach.id == 0 ? 'Thêm' : 'Sửa'} khách hàng</h2>
    <p style="color:red">${loi}</p>
    <form action="${base}" method="post">
        <input type="hidden" name="action" value="luu">
        <input type="hidden" name="id" value="${khach.id == 0 ? '' : khach.id}">
        <div>Họ tên: <input type="text" name="hoTen" value="<c:out value='${khach.hoTen}'/>" required></div>
        <div>SĐT: <input type="text" name="sdt" value="<c:out value='${khach.sdt}'/>" required></div>
        <div>CCCD: <input type="text" name="cccd" value="<c:out value='${khach.cccd}'/>"></div>
        <div>Email: <input type="text" name="email" value="<c:out value='${khach.email}'/>"></div>
        <div>Địa chỉ: <input type="text" name="diaChi" value="<c:out value='${khach.diaChi}'/>"></div>
        <button type="submit">Lưu</button>
        <a href="${base}">Hủy</a>
    </form>
</body>
</html>