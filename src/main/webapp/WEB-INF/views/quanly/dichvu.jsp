<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Dịch vụ</title></head>
<body>
    <h2>Danh mục dịch vụ</h2>
    <p style="color:red">${loi}</p>
    <a href="${pageContext.request.contextPath}/quanly/dichvu?action=them">+ Thêm dịch vụ</a>
    <table border="1" cellpadding="6">
        <tr><th>ID</th><th>Tên dịch vụ</th><th>Loại</th><th>Đơn giá</th><th>Trạng thái</th><th></th></tr>
        <c:forEach var="s" items="${dsDichVu}">
            <tr>
                <td>${s.id}</td>
                <td><c:out value="${s.tenDichVu}"/></td>
                <td>${s.loai}</td>
                <td>${s.donGia}</td>
                <td>${s.dangBan ? 'Đang bán' : 'Ngừng bán'}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/quanly/dichvu?action=sua&id=${s.id}">Sửa</a>
                    <form action="${pageContext.request.contextPath}/quanly/dichvu" method="post" style="display:inline"
                          onsubmit="return confirm('Xóa dịch vụ này?')">
                        <input type="hidden" name="action" value="xoa">
                        <input type="hidden" name="id" value="${s.id}">
                        <button type="submit">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>