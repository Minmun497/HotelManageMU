package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.Customer;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private Customer doc(ResultSet rs) throws SQLException {
        Customer k = new Customer();
        k.setId(rs.getInt("id"));
        k.setHoTen(rs.getString("ho_ten"));
        k.setCccd(rs.getString("cccd"));
        k.setSdt(rs.getString("sdt"));
        k.setEmail(rs.getString("email"));
        k.setDiaChi(rs.getString("dia_chi"));
        int tk = rs.getInt("tai_khoan_id");
        k.setTaiKhoanId(rs.wasNull() ? null : tk);
        return k;
    }

    public List<Customer> layTatCa() throws SQLException {
        List<Customer> ds = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM khach_hang ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
        }
        return ds;
    }

    public Customer layTheoId(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM khach_hang WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? doc(rs) : null;
            }
        }
    }

    // idBoQua: khi sửa thì bỏ qua chính khách đang sửa (thêm mới truyền 0)
    public boolean tonTaiSdt(String sdt, int idBoQua) throws SQLException {
        return tonTai("sdt", sdt, idBoQua);
    }

    public boolean tonTaiCccd(String cccd, int idBoQua) throws SQLException {
        return tonTai("cccd", cccd, idBoQua);
    }

    private boolean tonTai(String cot, String giaTri, int idBoQua) throws SQLException {
        String sql = "SELECT 1 FROM khach_hang WHERE " + cot + " = ? AND id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, giaTri);
            ps.setInt(2, idBoQua);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void them(Customer k) throws SQLException {
        String sql = "INSERT INTO khach_hang (ho_ten, cccd, sdt, email, dia_chi) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getHoTen());
            ps.setString(2, k.getCccd());
            ps.setString(3, k.getSdt());
            ps.setString(4, k.getEmail());
            ps.setString(5, k.getDiaChi());
            ps.executeUpdate();
        }
    }

    public void sua(Customer k) throws SQLException {
        String sql = "UPDATE khach_hang SET ho_ten=?, cccd=?, sdt=?, email=?, dia_chi=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.getHoTen());
            ps.setString(2, k.getCccd());
            ps.setString(3, k.getSdt());
            ps.setString(4, k.getEmail());
            ps.setString(5, k.getDiaChi());
            ps.setInt(6, k.getId());
            ps.executeUpdate();
        }
    }

    // Chỉ xóa khách KHÔNG có tài khoản đăng nhập. Trả về true nếu xóa được.
    public boolean xoa(int id) throws SQLException {
        String sql = "DELETE FROM khach_hang WHERE id = ? AND tai_khoan_id IS NULL";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}