package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.RoomTypeDAO;
import com.mycompany.hotelmumanage.model.RoomType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/quanly/loaiphong")
public class ManageRoomTypeServlet extends HttpServlet {

    private final RoomTypeDAO dao = new RoomTypeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("them".equals(action)) {
                hienForm(req, resp);
            } else if ("sua".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("loaiPhong", dao.layTheoId(id));
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
                    req.setAttribute("loi", "Không xóa được: loại phòng này đang có phòng sử dụng");
                    hienDanhSach(req, resp);
                    return;
                }
            } else if ("luu".equals(action)) {
                RoomType r = new RoomType();
                r.setTenLoai(req.getParameter("tenLoai").trim());
                r.setHinhThuc(req.getParameter("hinhThuc"));
                r.setMoTa(req.getParameter("moTa").trim());

                String loi = null;
                try {
                    r.setDonGia(Long.parseLong(req.getParameter("donGia")));
                    r.setSucChua(Integer.parseInt(req.getParameter("sucChua")));
                    if (r.getTenLoai().isEmpty()) loi = "Tên loại phòng không được để trống";
                    else if (r.getDonGia() <= 0) loi = "Đơn giá phải lớn hơn 0";
                    else if (r.getSucChua() <= 0) loi = "Sức chứa phải lớn hơn 0";
                } catch (NumberFormatException e) {
                    loi = "Đơn giá và sức chứa phải là số";
                }

                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) r.setId(Integer.parseInt(idStr));

                if (loi != null) {
                    req.setAttribute("loi", loi);
                    req.setAttribute("loaiPhong", r);
                    hienForm(req, resp);
                    return;
                }
                if (r.getId() == 0) dao.them(r); else dao.sua(r);
            }
            resp.sendRedirect(req.getContextPath() + "/quanly/loaiphong");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("dsLoaiPhong", dao.layTatCa());
        req.getRequestDispatcher("/WEB-INF/views/quanly/loaiphong.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/quanly/loaiphong-form.jsp").forward(req, resp);
    }
}