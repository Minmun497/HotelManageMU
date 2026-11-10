package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.User;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    public User dangNhap(String tenDangNhap, String matKhau) throws SQLException {
        String sql = "SELECT * FROM tai_khoan WHERE ten_dang_nhap = ? AND trang_thai = 'HOAT_DONG'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && BCrypt.checkpw(matKhau, rs.getString("mat_khau"))) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setTenDangNhap(rs.getString("ten_dang_nhap"));
                    u.setVaiTro(rs.getString("vai_tro"));
                    u.setTrangThai(rs.getString("trang_thai"));
                    return u;  // cố ý không set matKhau
                }
            }
        }
        return null;
    }
    public boolean tonTaiTenDangNhap(String tenDangNhap) throws SQLException {
    String sql = "SELECT 1 FROM tai_khoan WHERE ten_dang_nhap = ?";
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, tenDangNhap);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }
}

public boolean tonTaiSdt(String sdt) throws SQLException {
    String sql = "SELECT 1 FROM khach_hang WHERE sdt = ?";
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, sdt);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }
}

public void dangKyKhach(String tenDangNhap, String matKhau, String hoTen,
                        String sdt, String email) throws SQLException {
    String sqlTk = "INSERT INTO tai_khoan (ten_dang_nhap, mat_khau, vai_tro) VALUES (?, ?, 'KHACH_HANG')";
    String sqlKh = "INSERT INTO khach_hang (ho_ten, sdt, email, tai_khoan_id) VALUES (?, ?, ?, ?)";

    try (Connection c = DBConnection.getConnection()) {
        c.setAutoCommit(false);
        try {
            int taiKhoanId;
            try (PreparedStatement ps = c.prepareStatement(sqlTk, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, tenDangNhap);
                ps.setString(2, BCrypt.hashpw(matKhau, BCrypt.gensalt()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    taiKhoanId = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = c.prepareStatement(sqlKh)) {
                ps.setString(1, hoTen);
                ps.setString(2, sdt);
                ps.setString(3, email);
                ps.setInt(4, taiKhoanId);
                ps.executeUpdate();
            }
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        }
    }
    }
}
