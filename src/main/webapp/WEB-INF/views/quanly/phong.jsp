<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Phòng</title></head>
<body>
    <h2>Quản lý phòng</h2>
    <p style="color:red">${loi}</p>
    <a href="${pageContext.request.contextPath}/quanly/phong?action=them">+ Thêm phòng</a>
    <table border="1" cellpadding="6">
        <tr><th>Số phòng</th><th>Tầng</th><th>Loại phòng</th><th>Trạng thái</th><th></th></tr>
        <c:forEach var="p" items="${dsPhong}">
            <tr>
                <td>${p.soPhong}</td>
                <td>${p.tang}</td>
                <td>${p.tenLoai}</td>
                <td>${p.trangThai}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/quanly/phong?action=sua&id=${p.id}">Sửa</a>
                    <form action="${pageContext.request.contextPath}/quanly/phong" method="post" style="display:inline"
                          onsubmit="return confirm('Xóa phòng này?')">
                        <input type="hidden" name="action" value="xoa">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>