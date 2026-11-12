<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Phòng trống</title></head>
<body>
    <h2>Tìm phòng trống</h2>
    <p style="color:red">${loi}</p>
    <p style="color:green">${param.dat == 'ok' ? 'Đặt phòng thành công' : ''}</p>
    <form action="${base}" method="get">
        Ngày đến: <input type="date" name="ngayDen" value="<c:out value='${ngayDen}'/>" required>
        Ngày đi: <input type="date" name="ngayDi" value="<c:out value='${ngayDi}'/>" required>
        Loại phòng:
        <select name="loaiPhongId">
            <option value="0">Tất cả</option>
            <c:forEach var="loai" items="${dsLoaiPhong}">
                <c:if test="${loai.hinhThuc == 'QUA_DEM'}">
                    <option value="${loai.id}" ${loaiPhongId == loai.id ? 'selected' : ''}>${loai.tenLoai}</option>
                </c:if>
            </c:forEach>
        </select>
        <button type="submit">Tìm</button>
    </form>

    <c:if test="${dsPhongTrong != null}">
        <p>Thời gian ở: ${soDem} đêm (nhận phòng 14:00, trả phòng 12:00)</p>
        <c:if test="${empty dsPhongTrong}">
            <p>Không còn phòng trống trong khoảng ngày này.</p>
        </c:if>
        <c:if test="${not empty dsPhongTrong}">
            <table border="1" cellpadding="6">
                <tr><th>Số phòng</th><th>Tầng</th><th>Loại phòng</th><th>Giá một đêm</th></tr>
                <c:forEach var="p" items="${dsPhongTrong}">
                    <tr>
                        <td>${p.soPhong}</td>
                        <td>${p.tang}</td>
                        <td>${p.tenLoai}</td>
                        <td>${p.donGia}</td>
                        <c:if test="${sessionScope.user.vaiTro != 'KHACH_HANG'}">
                        <td><a href="${datPhongBase}?phongId=${p.id}&ngayDen=${ngayDen}&ngayDi=${ngayDi}">Đặt phòng</a></td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:if>
</body>
</html>