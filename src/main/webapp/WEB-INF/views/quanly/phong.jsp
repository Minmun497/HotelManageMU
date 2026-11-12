<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Phòng</title></head>
<body>
    <h2>Quản lý phòng</h2>
    <p style="color:red">${loi}</p>

    <form action="${pageContext.request.contextPath}/quanly/phong" method="get">
        Số phòng: <input type="text" name="tuKhoa" value="${tuKhoa}">
        Loại phòng:
        <select name="loaiPhongId">
            <option value="0">Tất cả</option>
            <c:forEach var="loai" items="${dsLoaiPhong}">
                <option value="${loai.id}" ${loaiPhongId == loai.id ? 'selected' : ''}>${loai.tenLoai}</option>
            </c:forEach>
        </select>
        Trạng thái:
        <select name="trangThai">
            <option value="">Tất cả</option>
            <option value="TRONG" ${trangThai == 'TRONG' ? 'selected' : ''}>Trống</option>
            <option value="DANG_O" ${trangThai == 'DANG_O' ? 'selected' : ''}>Đang ở</option>
            <option value="DANG_DON_DEP" ${trangThai == 'DANG_DON_DEP' ? 'selected' : ''}>Đang dọn dẹp</option>
            <option value="BAO_TRI" ${trangThai == 'BAO_TRI' ? 'selected' : ''}>Bảo trì</option>
        </select>
        <button type="submit">Lọc</button>
        <a href="${pageContext.request.contextPath}/quanly/phong">Xóa lọc</a>
    </form>

    <p>Tìm thấy ${tongSo} phòng</p>
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

    <c:if test="${tongTrang > 1}">
        <c:url var="base" value="/quanly/phong">
            <c:param name="tuKhoa" value="${tuKhoa}" />
            <c:param name="trangThai" value="${trangThai}" />
            <c:param name="loaiPhongId" value="${loaiPhongId}" />
        </c:url>
        <p>
            Trang:
            <c:forEach begin="1" end="${tongTrang}" var="i">
                <c:choose>
                    <c:when test="${i == trang}"><b>[${i}]</b></c:when>
                    <c:otherwise><a href="${base}&trang=${i}">${i}</a></c:otherwise>
                </c:choose>
            </c:forEach>
        </p>
    </c:if>
</body>
</html>