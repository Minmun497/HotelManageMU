package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.Service;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    private Service doc(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setId(rs.getInt("id"));
        s.setTenDichVu(rs.getString("ten_dich_vu"));
        s.setLoai(rs.getString("loai"));
        s.setDonGia(rs.getLong("don_gia"));
        s.setDangBan(rs.getBoolean("dang_ban"));
        return s;
    }

    public List<Service> layTatCa() throws SQLException {
        List<Service> ds = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM dich_vu ORDER BY loai, id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
        }
        return ds;
    }

    public Service layTheoId(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM dich_vu WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? doc(rs) : null;
            }
        }
    }

    public void them(Service s) throws SQLException {
        String sql = "INSERT INTO dich_vu (ten_dich_vu, loai, don_gia, dang_ban) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getTenDichVu());
            ps.setString(2, s.getLoai());
            ps.setLong(3, s.getDonGia());
            ps.setBoolean(4, s.isDangBan());
            ps.executeUpdate();
        }
    }

    public void sua(Service s) throws SQLException {
        String sql = "UPDATE dich_vu SET ten_dich_vu=?, loai=?, don_gia=?, dang_ban=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getTenDichVu());
            ps.setString(2, s.getLoai());
            ps.setLong(3, s.getDonGia());
            ps.setBoolean(4, s.isDangBan());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM dich_vu WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}