<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Dịch vụ</title></head>
<body>
    <h2>${empty dichVu || dichVu.id == 0 ? 'Thêm' : 'Sửa'} dịch vụ</h2>
    <p style="color:red">${loi}</p>
    <form action="${pageContext.request.contextPath}/quanly/dichvu" method="post">
        <input type="hidden" name="action" value="luu">
        <input type="hidden" name="id" value="${dichVu.id == 0 ? '' : dichVu.id}">
        <div>Tên dịch vụ: <input type="text" name="tenDichVu" value="<c:out value='${dichVu.tenDichVu}'/>" required></div>
        <div>Loại:
            <select name="loai">
                <option value="PHONG" ${dichVu.loai == 'PHONG' ? 'selected' : ''}>Dịch vụ phòng</option>
                <option value="SPA" ${dichVu.loai == 'SPA' ? 'selected' : ''}>Spa</option>
                <option value="PHONG_HOP" ${dichVu.loai == 'PHONG_HOP' ? 'selected' : ''}>Phòng họp</option>
                <option value="DUA_DON" ${dichVu.loai == 'DUA_DON' ? 'selected' : ''}>Đưa đón</option>
            </select>
        </div>
        <c:set var="giaHienThi" value="${not empty donGiaNhap ? donGiaNhap : (dichVu.donGia == 0 ? '' : dichVu.donGia)}"/>
        <div>Đơn giá: <input type="text" name="donGia" value="<c:out value='${giaHienThi}'/>" required></div>
        <div>
            <label><input type="checkbox" name="dangBan" ${empty dichVu || dichVu.dangBan ? 'checked' : ''}> Đang bán</label>
        </div>
        <button type="submit">Lưu</button>
        <a href="${pageContext.request.contextPath}/quanly/dichvu">Hủy</a>
    </form>
</body>
</html>