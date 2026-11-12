package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.Booking;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;

public class BookingDAO {

    // Trả về true nếu đặt được, false nếu phòng không còn trống (hoặc không đặt được)
    public boolean taoDatPhong(Booking b, int taiKhoanId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                // 1. Khóa dòng phòng, kiểm tra là phòng qua đêm và không bảo trì
                String sqlKhoa = "SELECT p.trang_thai, l.hinh_thuc FROM phong p "
                        + "JOIN loai_phong l ON p.loai_phong_id = l.id WHERE p.id = ? FOR UPDATE";
                try (PreparedStatement ps = c.prepareStatement(sqlKhoa)) {
                    ps.setInt(1, b.getPhongId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()
                                || "BAO_TRI".equals(rs.getString("trang_thai"))
                                || !"QUA_DEM".equals(rs.getString("hinh_thuc"))) {
                            c.rollback();
                            return false;
                        }
                    }
                }

                // 2. Kiểm tra có đặt phòng nào chồng khoảng ngày không
                String sqlTrung = "SELECT COUNT(*) FROM dat_phong WHERE phong_id = ? "
                        + "AND trang_thai IN ('CHO_XAC_NHAN', 'DA_XAC_NHAN', 'DA_NHAN_PHONG') "
                        + "AND ngay_den < ? AND ngay_di > ?";
                try (PreparedStatement ps = c.prepareStatement(sqlTrung)) {
                    ps.setInt(1, b.getPhongId());
                    ps.setTimestamp(2, Timestamp.valueOf(b.getNgayDi()));
                    ps.setTimestamp(3, Timestamp.valueOf(b.getNgayDen()));
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            c.rollback();
                            return false;
                        }
                    }
                }

                // 3. Thêm đặt phòng (nhân viên tạo thì xác nhận luôn)
                String sqlThem = "INSERT INTO dat_phong (khach_hang_id, phong_id, nhan_vien_id, "
                        + "ngay_den, ngay_di, tien_coc, trang_thai, nguon) "
                        + "VALUES (?, ?, (SELECT id FROM nhan_vien WHERE tai_khoan_id = ?), ?, ?, ?, 'DA_XAC_NHAN', 'LE_TAN')";
                try (PreparedStatement ps = c.prepareStatement(sqlThem)) {
                    ps.setInt(1, b.getKhachHangId());
                    ps.setInt(2, b.getPhongId());
                    ps.setInt(3, taiKhoanId);
                    ps.setTimestamp(4, Timestamp.valueOf(b.getNgayDen()));
                    ps.setTimestamp(5, Timestamp.valueOf(b.getNgayDi()));
                    ps.setLong(6, b.getTienCoc());
                    ps.executeUpdate();
                }

                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}