package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.ServiceDAO;
import com.mycompany.hotelmumanage.model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@WebServlet("/quanly/dichvu")
public class ManageServiceServlet extends HttpServlet {

    private static final List<String> LOAI = List.of("PHONG", "SPA", "PHONG_HOP", "DUA_DON");
    private final ServiceDAO dao = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("them".equals(action)) {
                hienForm(req, resp);
            } else if ("sua".equals(action)) {
                Service s = dao.layTheoId(Integer.parseInt(req.getParameter("id")));
                if (s == null) {
                    resp.sendRedirect(req.getContextPath() + "/quanly/dichvu");
                    return;
                }
                req.setAttribute("dichVu", s);
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
                    req.setAttribute("loi", "Không xóa được: dịch vụ đã được sử dụng. Hãy bỏ tick \"Đang bán\" để ngừng bán.");
                    hienDanhSach(req, resp);
                    return;
                }
            } else if ("luu".equals(action)) {
                Service s = new Service();
                s.setTenDichVu(req.getParameter("tenDichVu").trim());
                s.setLoai(req.getParameter("loai"));
                s.setDangBan(req.getParameter("dangBan") != null);
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) s.setId(Integer.parseInt(idStr));

                String loi = null;
                try {
                    s.setDonGia(Long.parseLong(req.getParameter("donGia").trim()));
                    if (s.getTenDichVu().isEmpty()) loi = "Tên dịch vụ không được để trống";
                    else if (s.getTenDichVu().length() > 100) loi = "Tên dịch vụ tối đa 100 ký tự";
                    else if (!LOAI.contains(s.getLoai())) loi = "Loại dịch vụ không hợp lệ";
                    else if (s.getDonGia() <= 0 || s.getDonGia() > 999_999_999_999L) loi = "Đơn giá phải từ 1 đến 999.999.999.999";
                } catch (NumberFormatException e) {
                    loi = "Đơn giá phải là số";
                }

                if (loi != null) {
                    req.setAttribute("loi", loi);
                    req.setAttribute("dichVu", s);
                    req.setAttribute("donGiaNhap", req.getParameter("donGia"));   // thêm dòng này
                    hienForm(req, resp);
                return;
                }
                if (s.getId() == 0) dao.them(s); else dao.sua(s);
            }
            resp.sendRedirect(req.getContextPath() + "/quanly/dichvu");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("dsDichVu", dao.layTatCa());
        req.getRequestDispatcher("/WEB-INF/views/quanly/dichvu.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/quanly/dichvu-form.jsp").forward(req, resp);
    }
}