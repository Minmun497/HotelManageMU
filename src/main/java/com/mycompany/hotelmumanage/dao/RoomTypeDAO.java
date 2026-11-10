package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.RoomType;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {

    private RoomType doc(ResultSet rs) throws SQLException {
        RoomType r = new RoomType();
        r.setId(rs.getInt("id"));
        r.setTenLoai(rs.getString("ten_loai"));
        r.setHinhThuc(rs.getString("hinh_thuc"));
        r.setDonGia(rs.getLong("don_gia"));
        r.setSucChua(rs.getInt("suc_chua"));
        r.setMoTa(rs.getString("mo_ta"));
        return r;
    }

    public List<RoomType> layTatCa() throws SQLException {
        List<RoomType> ds = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM loai_phong ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
        }
        return ds;
    }

    public RoomType layTheoId(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM loai_phong WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? doc(rs) : null;
            }
        }
    }

    public void them(RoomType r) throws SQLException {
        String sql = "INSERT INTO loai_phong (ten_loai, hinh_thuc, don_gia, suc_chua, mo_ta) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getTenLoai());
            ps.setString(2, r.getHinhThuc());
            ps.setLong(3, r.getDonGia());
            ps.setInt(4, r.getSucChua());
            ps.setString(5, r.getMoTa());
            ps.executeUpdate();
        }
    }

    public void sua(RoomType r) throws SQLException {
        String sql = "UPDATE loai_phong SET ten_loai=?, hinh_thuc=?, don_gia=?, suc_chua=?, mo_ta=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getTenLoai());
            ps.setString(2, r.getHinhThuc());
            ps.setLong(3, r.getDonGia());
            ps.setInt(4, r.getSucChua());
            ps.setString(5, r.getMoTa());
            ps.setInt(6, r.getId());
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM loai_phong WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}