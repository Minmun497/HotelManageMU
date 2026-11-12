package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.RoomDAO;
import com.mycompany.hotelmumanage.dao.RoomTypeDAO;
import com.mycompany.hotelmumanage.model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@WebServlet("/quanly/phong")
public class ManageRoomServlet extends HttpServlet {

    private static final List<String> TRANG_THAI = List.of("TRONG", "DANG_O", "DANG_DON_DEP", "BAO_TRI");
    private final RoomDAO dao = new RoomDAO();
    private final RoomTypeDAO loaiDao = new RoomTypeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("them".equals(action)) {
                hienForm(req, resp);
            } else if ("sua".equals(action)) {
                req.setAttribute("phong", dao.layTheoId(Integer.parseInt(req.getParameter("id"))));
                hienForm(req, resp);
            } else {
                hienDanhSach(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("xoa".equals(action)) {
                try {
                    dao.xoa(Integer.parseInt(req.getParameter("id")));
                } catch (SQLIntegrityConstraintViolationException e) {
                    req.setAttribute("loi", "Không xóa được: phòng này đã có lịch sử đặt hoặc sử dụng");
                    hienDanhSach(req, resp);
                    return;
                }
            } else if ("luu".equals(action)) {
                Room r = new Room();
                r.setSoPhong(req.getParameter("soPhong").trim());
                r.setTrangThai(req.getParameter("trangThai"));

                String loi = null;
                try {
                    String tangStr = req.getParameter("tang").trim();
                    r.setTang(tangStr.isEmpty() ? null : Integer.valueOf(tangStr));
                    r.setLoaiPhongId(Integer.parseInt(req.getParameter("loaiPhongId")));
                } catch (NumberFormatException e) {
                    loi = "Tầng phải là số và phải chọn loại phòng";
                }

                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) r.setId(Integer.parseInt(idStr));

                if (loi == null) {
                    if (r.getSoPhong().isEmpty()) loi = "Số phòng không được để trống";
                    else if (!TRANG_THAI.contains(r.getTrangThai())) loi = "Trạng thái không hợp lệ";
                    else if (dao.tonTaiSoPhong(r.getSoPhong(), r.getId())) loi = "Số phòng đã tồn tại";
                }

                if (loi != null) {
                    req.setAttribute("loi", loi);
                    req.setAttribute("phong", r);
                    req.setAttribute("tangNhap", req.getParameter("tang"));
                    hienForm(req, resp);
                    return;
                }
                if (r.getId() == 0) dao.them(r); else dao.sua(r);
            }
            resp.sendRedirect(req.getContextPath() + "/quanly/phong");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static final int SO_DONG = 2;   // số phòng mỗi trang (đặt cạnh TRANG_THAI)

private int soNguyen(String s, int macDinh) {
    try {
        return Integer.parseInt(s);
    } catch (Exception e) {
        return macDinh;
    }
}

private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, ServletException, IOException {
    String tuKhoa = req.getParameter("tuKhoa");
    if (tuKhoa == null) tuKhoa = "";
    tuKhoa = tuKhoa.trim();
    String trangThai = req.getParameter("trangThai");
    if (trangThai == null || !TRANG_THAI.contains(trangThai)) trangThai = "";
    int loaiId = soNguyen(req.getParameter("loaiPhongId"), 0);
    int trang = soNguyen(req.getParameter("trang"), 1);

    int tongSo = dao.dem(tuKhoa, trangThai, loaiId);
    int tongTrang = Math.max(1, (int) Math.ceil((double) tongSo / SO_DONG));
    if (trang < 1) trang = 1;
    if (trang > tongTrang) trang = tongTrang;

    req.setAttribute("dsPhong", dao.timKiem(tuKhoa, trangThai, loaiId, trang, SO_DONG));
    req.setAttribute("dsLoaiPhong", loaiDao.layTatCa());
    req.setAttribute("tuKhoa", tuKhoa);
    req.setAttribute("trangThai", trangThai);
    req.setAttribute("loaiPhongId", loaiId);
    req.setAttribute("trang", trang);
    req.setAttribute("tongTrang", tongTrang);
    req.setAttribute("tongSo", tongSo);
    req.getRequestDispatcher("/WEB-INF/views/quanly/phong.jsp").forward(req, resp);
}

    private void hienForm(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("dsLoaiPhong", loaiDao.layTatCa());   // để vẽ ô chọn loại phòng
        req.getRequestDispatcher("/WEB-INF/views/quanly/phong-form.jsp").forward(req, resp);
    }
}