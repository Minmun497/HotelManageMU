<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Loại phòng</title></head>
<body>
    <h2>${empty loaiPhong || loaiPhong.id == 0 ? 'Thêm' : 'Sửa'} loại phòng</h2>
    <p style="color:red">${loi}</p>
    <form action="${pageContext.request.contextPath}/quanly/loaiphong" method="post">
        <input type="hidden" name="action" value="luu">
        <input type="hidden" name="id" value="${loaiPhong.id == 0 ? '' : loaiPhong.id}">
        <div>Tên loại: <input type="text" name="tenLoai" value="${loaiPhong.tenLoai}" required></div>
        <div>Hình thức:
            <select name="hinhThuc">
                <option value="QUA_DEM" ${loaiPhong.hinhThuc == 'QUA_DEM' ? 'selected' : ''}>Qua đêm</option>
                <option value="THEO_GIO" ${loaiPhong.hinhThuc == 'THEO_GIO' ? 'selected' : ''}>Theo giờ</option>
            </select>
        </div>
        <div>Đơn giá: <input type="text" name="donGia" value="${loaiPhong.donGia == 0 ? '' : loaiPhong.donGia}" required></div>
        <div>Sức chứa: <input type="text" name="sucChua" value="${loaiPhong.sucChua == 0 ? '' : loaiPhong.sucChua}" required></div>
        <div>Mô tả: <input type="text" name="moTa" value="${loaiPhong.moTa}"></div>
        <button type="submit">Lưu</button>
        <a href="${pageContext.request.contextPath}/quanly/loaiphong">Hủy</a>
    </form>
</body>
</html>