<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Khách hàng</title></head>
<body>
    <h2>Quản lý khách hàng</h2>
    <p style="color:red">${loi}</p>
    <a href="${base}?action=them">+ Thêm khách hàng</a>
    <table border="1" cellpadding="6">
        <tr><th>ID</th><th>Họ tên</th><th>CCCD</th><th>SĐT</th><th>Email</th><th>Địa chỉ</th><th>Tài khoản</th><th></th></tr>
        <c:forEach var="k" items="${dsKhach}">
            <tr>
                <td>${k.id}</td>
                <td><c:out value="${k.hoTen}"/></td>
                <td><c:out value="${k.cccd}"/></td>
                <td><c:out value="${k.sdt}"/></td>
                <td><c:out value="${k.email}"/></td>
                <td><c:out value="${k.diaChi}"/></td>
                <td>${empty k.taiKhoanId ? 'Không' : 'Có'}</td>
                <td>
                    <a href="${base}?action=sua&id=${k.id}">Sửa</a>
                    <c:if test="${sessionScope.user.vaiTro == 'QUAN_LY'}">
                        <form action="${base}" method="post" style="display:inline"
                              onsubmit="return confirm('Xóa khách này?')">
                            <input type="hidden" name="action" value="xoa">
                            <input type="hidden" name="id" value="${k.id}">
                            <button type="submit">Xóa</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>