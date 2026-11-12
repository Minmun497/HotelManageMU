package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.BookingDAO;
import com.mycompany.hotelmumanage.dao.CustomerDAO;
import com.mycompany.hotelmumanage.dao.RoomDAO;
import com.mycompany.hotelmumanage.model.Booking;
import com.mycompany.hotelmumanage.model.Room;
import com.mycompany.hotelmumanage.model.User;
import com.mycompany.hotelmumanage.util.GioQuyDinh;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@WebServlet({"/quanly/datphong", "/letan/datphong"})
public class ManageBookingServlet extends HttpServlet {

    private final RoomDAO roomDao = new RoomDAO();
    private final CustomerDAO customerDao = new CustomerDAO();
    private final BookingDAO bookingDao = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        try {
            if (!chuanBi(req)) {
                resp.sendRedirect(diTimPhong(req));
                return;
            }
            hienForm(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        try {
            if (!chuanBi(req)) {
                resp.sendRedirect(diTimPhong(req));
                return;
            }
            Room phong = (Room) req.getAttribute("phong");
            long tamTinh = (Long) req.getAttribute("tamTinh");

            int khachId = soNguyen(req.getParameter("khachHangId"), 0);
            String cocStr = req.getParameter("tienCoc");
            cocStr = (cocStr == null) ? "" : cocStr.trim();
            long coc = 0;
            String loi = null;
            try {
                if (!cocStr.isEmpty()) coc = Long.parseLong(cocStr);
            } catch (NumberFormatException e) {
                loi = "Tiền cọc phải là số";
            }
            if (loi == null) {
                if (khachId <= 0 || customerDao.layTheoId(khachId) == null) {
                    loi = "Vui lòng chọn khách hàng";
                } else if (coc < 0 || coc > tamTinh) {
                    loi = "Tiền cọc phải từ 0 đến " + tamTinh;
                }
            }
            if (loi != null) {
                baoLoi(req, resp, loi, khachId, cocStr);
                return;
            }

            LocalDate den = LocalDate.parse(req.getParameter("ngayDen"));
            LocalDate di = LocalDate.parse(req.getParameter("ngayDi"));
            Booking b = new Booking();
            b.setKhachHangId(khachId);
            b.setPhongId(phong.getId());
            b.setNgayDen(den.atTime(GioQuyDinh.GIO_NHAN, 0));
            b.setNgayDi(di.atTime(GioQuyDinh.GIO_TRA, 0));
            b.setTienCoc(coc);

            User user = (User) req.getSession().getAttribute("user");
            if (!bookingDao.taoDatPhong(b, user.getId())) {
                baoLoi(req, resp, "Phòng không còn trống trong khoảng ngày này (có thể vừa có người đặt)", khachId, cocStr);
                return;
            }
            resp.sendRedirect(diTimPhong(req) + "?dat=ok");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Đọc phòng + ngày từ địa chỉ/form, kiểm tra, tính tạm tính. Sai thì trả false.
    private boolean chuanBi(HttpServletRequest req) throws SQLException {
        try {
            int phongId = Integer.parseInt(req.getParameter("phongId"));
            LocalDate den = LocalDate.parse(req.getParameter("ngayDen"));
            LocalDate di = LocalDate.parse(req.getParameter("ngayDi"));
            Room phong = roomDao.layTheoId(phongId);
            if (phong == null || den.isBefore(LocalDate.now()) || !di.isAfter(den)) return false;

            long soDem = ChronoUnit.DAYS.between(den, di);
            req.setAttribute("phong", phong);
            req.setAttribute("ngayDen", den.toString());
            req.setAttribute("ngayDi", di.toString());
            req.setAttribute("soDem", soDem);
            req.setAttribute("tamTinh", soDem * phong.getDonGia());
            req.setAttribute("dsKhach", customerDao.layTatCa());
            return true;
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    private void baoLoi(HttpServletRequest req, HttpServletResponse resp, String loi, int khachId, String cocStr)
            throws ServletException, IOException {
        req.setAttribute("loi", loi);
        req.setAttribute("khachChon", khachId);
        req.setAttribute("tienCocNhap", cocStr);
        hienForm(req, resp);
    }

    private String diTimPhong(HttpServletRequest req) {
        return req.getContextPath() + req.getServletPath().replace("datphong", "phongtrong");
    }

    private int soNguyen(String s, int macDinh) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return macDinh;
        }
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/datphong/datphong-form.jsp").forward(req, resp);
    }
}