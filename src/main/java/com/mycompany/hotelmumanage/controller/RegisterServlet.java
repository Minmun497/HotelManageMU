package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tenDangNhap = req.getParameter("tenDangNhap").trim();
        String matKhau = req.getParameter("matKhau");
        String xacNhan = req.getParameter("xacNhanMatKhau");
        String hoTen = req.getParameter("hoTen").trim();
        String sdt = req.getParameter("sdt").trim();
        String email = req.getParameter("email").trim();

        String loi = null;
        try {
            UserDAO dao = new UserDAO();
            if (tenDangNhap.isEmpty() || hoTen.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
                loi = "Vui lòng điền đủ họ tên, SĐT, tên đăng nhập và mật khẩu";
            } else if (matKhau.length() < 6) {
                loi = "Mật khẩu phải từ 6 ký tự trở lên";
            } else if (!matKhau.equals(xacNhan)) {
                loi = "Mật khẩu nhập lại không khớp";
            } else if (dao.tonTaiTenDangNhap(tenDangNhap)) {
                loi = "Tên đăng nhập đã được dùng";
            } else if (dao.tonTaiSdt(sdt)) {
                loi = "Số điện thoại đã được đăng ký";
            }

            if (loi != null) {
                req.setAttribute("loi", loi);
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            dao.dangKyKhach(tenDangNhap, matKhau, hoTen, sdt, email);
            resp.sendRedirect(req.getContextPath() + "/login?dk=ok");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}