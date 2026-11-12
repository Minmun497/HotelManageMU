<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Đặt phòng</title></head>
<body>
    <h2>Đặt phòng</h2>
    <p style="color:red">${loi}</p>

    <p>Phòng: <b>${phong.soPhong}</b> (${phong.tenLoai}, tầng ${phong.tang})</p>
    <p>Nhận phòng: ${ngayDen} 14:00 &nbsp;&nbsp; Trả phòng: ${ngayDi} 12:00</p>
    <p>${soDem} đêm × ${phong.donGia} = <b>${tamTinh}</b> (tạm tính)</p>

    <form action="${base}" method="post">
        <input type="hidden" name="phongId" value="${phong.id}">
        <input type="hidden" name="ngayDen" value="${ngayDen}">
        <input type="hidden" name="ngayDi" value="${ngayDi}">
        <div>Khách hàng:
            <select name="khachHangId">
                <option value="0">-- Chọn khách --</option>
                <c:forEach var="k" items="${dsKhach}">
                    <option value="${k.id}" ${khachChon == k.id ? 'selected' : ''}><c:out value="${k.hoTen}"/> - <c:out value="${k.sdt}"/></option>
                </c:forEach>
            </select>
        </div>
        <div>Tiền cọc: <input type="text" name="tienCoc" value="<c:out value='${tienCocNhap}'/>"></div>
        <button type="submit">Đặt phòng</button>
        <a href="${pageContext.request.contextPath}/${sessionScope.user.vaiTro == 'QUAN_LY' ? 'quanly' : 'letan'}/phongtrong">Hủy</a>
    </form>
</body>
</html>