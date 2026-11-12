package com.mycompany.hotelmumanage.controller;

import com.mycompany.hotelmumanage.dao.RoomDAO;
import com.mycompany.hotelmumanage.dao.RoomTypeDAO;
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

@WebServlet({"/quanly/phongtrong", "/letan/phongtrong", "/khach/phongtrong"})
public class AvailableRoomServlet extends HttpServlet {

    private final RoomDAO dao = new RoomDAO();
    private final RoomTypeDAO loaiDao = new RoomTypeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        req.setAttribute("base", req.getContextPath() + req.getServletPath());
        req.setAttribute("datPhongBase", req.getContextPath() + req.getServletPath().replace("phongtrong", "datphong"));   // <-- dòng mới
        try {
            req.setAttribute("dsLoaiPhong", loaiDao.layTatCa());

            String denStr = req.getParameter("ngayDen");
            String diStr = req.getParameter("ngayDi");
            if (denStr != null && diStr != null) {          // người dùng đã bấm Tìm
                int loaiId = soNguyen(req.getParameter("loaiPhongId"), 0);
                req.setAttribute("ngayDen", denStr);
                req.setAttribute("ngayDi", diStr);
                req.setAttribute("loaiPhongId", loaiId);
                try {
                    LocalDate den = LocalDate.parse(denStr);
                    LocalDate di = LocalDate.parse(diStr);
                    if (den.isBefore(LocalDate.now())) {
                        req.setAttribute("loi", "Ngày đến không được ở trong quá khứ");
                    } else if (!di.isAfter(den)) {
                        req.setAttribute("loi", "Ngày đi phải sau ngày đến");
                    } else {
                        req.setAttribute("dsPhongTrong",
                            dao.timPhongTrong(den.atTime(GioQuyDinh.GIO_NHAN, 0), di.atTime(GioQuyDinh.GIO_TRA, 0), loaiId));
                        req.setAttribute("soDem", ChronoUnit.DAYS.between(den, di));
                    }
                } catch (DateTimeParseException e) {
                    req.setAttribute("loi", "Ngày không hợp lệ");
                }
            }
            req.getRequestDispatcher("/WEB-INF/views/phongtrong.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private int soNguyen(String s, int macDinh) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return macDinh;
        }
    }
}