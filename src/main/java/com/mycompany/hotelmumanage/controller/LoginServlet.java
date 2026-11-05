package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.UserDAO;
import com.mycompany.hotelmumanage.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tenDangNhap = req.getParameter("tenDangNhap");
        String matKhau = req.getParameter("matKhau");

        try {
            User user = new UserDAO().dangNhap(tenDangNhap, matKhau);

            if (user == null) {
                req.setAttribute("loi", "Sai tên đăng nhập hoặc mật khẩu");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            req.getSession().invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            String ctx = req.getContextPath();
            switch (user.getVaiTro()) {
            case "QUAN_LY" -> resp.sendRedirect(ctx + "/quanly/home");
            case "LE_TAN"  -> resp.sendRedirect(ctx + "/letan/home");
            default        -> resp.sendRedirect(ctx + "/khach/home");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}