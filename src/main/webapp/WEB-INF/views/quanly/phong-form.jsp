<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Phòng</title></head>
<body>
    <h2>${empty phong || phong.id == 0 ? 'Thêm' : 'Sửa'} phòng</h2>
    <p style="color:red">${loi}</p>
    <form action="${pageContext.request.contextPath}/quanly/phong" method="post">
        <input type="hidden" name="action" value="luu">
        <input type="hidden" name="id" value="${phong.id == 0 ? '' : phong.id}">
        <div>Số phòng: <input type="text" name="soPhong" value="${phong.soPhong}" required></div>
        <div>Tầng: <input type="text" name="tang" value="${phong.tang}"></div>
        <div>Loại phòng:
            <select name="loaiPhongId">
                <c:forEach var="loai" items="${dsLoaiPhong}">
                    <option value="${loai.id}" ${phong.loaiPhongId == loai.id ? 'selected' : ''}>${loai.tenLoai}</option>
                </c:forEach>
            </select>
        </div>
        <div>Trạng thái:
            <select name="trangThai">
                <option value="TRONG" ${phong.trangThai == 'TRONG' ? 'selected' : ''}>Trống</option>
                <option value="DANG_O" ${phong.trangThai == 'DANG_O' ? 'selected' : ''}>Đang ở</option>
                <option value="DANG_DON_DEP" ${phong.trangThai == 'DANG_DON_DEP' ? 'selected' : ''}>Đang dọn dẹp</option>
                <option value="BAO_TRI" ${phong.trangThai == 'BAO_TRI' ? 'selected' : ''}>Bảo trì</option>
            </select>
        </div>
        <button type="submit">Lưu</button>
        <a href="${pageContext.request.contextPath}/quanly/phong">Hủy</a>
    </form>
</body>
</html>