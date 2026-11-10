<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Loại phòng</title></head>
<body>
    <h2>Quản lý loại phòng</h2>
    <p style="color:red">${loi}</p>
    <a href="${pageContext.request.contextPath}/quanly/loaiphong?action=them">+ Thêm loại phòng</a>
    <table border="1" cellpadding="6">
        <tr><th>ID</th><th>Tên loại</th><th>Hình thức</th><th>Đơn giá</th><th>Sức chứa</th><th>Mô tả</th><th></th></tr>
        <c:forEach var="r" items="${dsLoaiPhong}">
            <tr>
                <td>${r.id}</td>
                <td>${r.tenLoai}</td>
                <td>${r.hinhThuc}</td>
                <td>${r.donGia}</td>
                <td>${r.sucChua}</td>
                <td>${r.moTa}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/quanly/loaiphong?action=sua&id=${r.id}">Sửa</a>
                    <form action="${pageContext.request.contextPath}/quanly/loaiphong" method="post" style="display:inline"
                          onsubmit="return confirm('Xóa loại phòng này?')">
                        <input type="hidden" name="action" value="xoa">
                        <input type="hidden" name="id" value="${r.id}">
                        <button type="submit">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>