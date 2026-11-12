package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.CustomerDAO;
import com.mycompany.hotelmumanage.model.Customer;
import com.mycompany.hotelmumanage.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet({"/quanly/khachhang", "/letan/khachhang"})
public class ManageCustomerServlet extends HttpServlet {

    private final CustomerDAO dao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        try {
            String action = req.getParameter("action");
            if ("them".equals(action)) {
                hienForm(req, resp);
            } else if ("sua".equals(action)) {
                Customer k = dao.layTheoId(Integer.parseInt(req.getParameter("id")));
                if (k == null) {
                    resp.sendRedirect(req.getContextPath() + req.getServletPath());
                    return;
                }
                req.setAttribute("khach", k);
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
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        try {
            String action = req.getParameter("action");
            if ("xoa".equals(action)) {
                if (!laQuanLy(req)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                try {
                    if (!dao.xoa(Integer.parseInt(req.getParameter("id")))) {
                        req.setAttribute("loi", "Không xóa được: khách này có tài khoản đăng nhập");
                        hienDanhSach(req, resp);
                        return;
                    }
                } catch (SQLIntegrityConstraintViolationException e) {
                    req.setAttribute("loi", "Không xóa được: khách này đã có đặt phòng");
                    hienDanhSach(req, resp);
                    return;
                }
            } else if ("luu".equals(action)) {
                Customer k = new Customer();
                k.setHoTen(hoacNull(req.getParameter("hoTen")));
                k.setCccd(hoacNull(req.getParameter("cccd")));
                k.setSdt(hoacNull(req.getParameter("sdt")));
                k.setEmail(hoacNull(req.getParameter("email")));
                k.setDiaChi(hoacNull(req.getParameter("diaChi")));
                String idStr = req.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) k.setId(Integer.parseInt(idStr));

                String loi = kiemTra(k);
                if (loi != null) {
                    req.setAttribute("loi", loi);
                    req.setAttribute("khach", k);
                    hienForm(req, resp);
                    return;
                }
                if (k.getId() == 0) dao.them(k); else dao.sua(k);
            }
            resp.sendRedirect(req.getContextPath() + req.getServletPath());
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    // Trả về câu báo lỗi đầu tiên tìm thấy, hoặc null nếu dữ liệu hợp lệ
    private String kiemTra(Customer k) throws SQLException {
        if (k.getHoTen() == null) return "Họ tên không được để trống";
        if (k.getSdt() == null) return "Số điện thoại không được để trống";
        if (!k.getSdt().matches("0\\d{9}")) return "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng 0";
        if (k.getCccd() != null && !k.getCccd().matches("\\d{12}")) return "CCCD phải gồm đúng 12 chữ số";
        if (k.getEmail() != null && !k.getEmail().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) return "Email không hợp lệ";
        if (dao.tonTaiSdt(k.getSdt(), k.getId())) return "Số điện thoại đã được dùng cho khách khác";
        if (k.getCccd() != null && dao.tonTaiCccd(k.getCccd(), k.getId())) return "CCCD đã được dùng cho khách khác";
        return null;
    }

    // Cắt khoảng trắng; chuỗi rỗng đổi thành null để cột UNIQUE (cccd) không bị trùng nhau
    private String hoacNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private boolean laQuanLy(HttpServletRequest req) {
        User u = (User) req.getSession().getAttribute("user");
        return u != null && "QUAN_LY".equals(u.getVaiTro());
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("dsKhach", dao.layTatCa());
        req.getRequestDispatcher("/WEB-INF/views/khachhang/khachhang.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/khachhang/khachhang-form.jsp").forward(req, resp);
    }
}