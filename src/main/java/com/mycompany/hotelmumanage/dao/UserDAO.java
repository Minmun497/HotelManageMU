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
}
