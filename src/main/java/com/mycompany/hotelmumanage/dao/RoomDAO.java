package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.Room;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private static final String SELECT_JOIN =
        "SELECT p.*, l.ten_loai FROM phong p JOIN loai_phong l ON p.loai_phong_id = l.id ";

    private Room doc(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setSoPhong(rs.getString("so_phong"));
        int tang = rs.getInt("tang");
        r.setTang(rs.wasNull() ? null : tang);
        r.setTrangThai(rs.getString("trang_thai"));
        r.setLoaiPhongId(rs.getInt("loai_phong_id"));
        r.setTenLoai(rs.getString("ten_loai"));
        return r;
    }

    public List<Room> layTatCa() throws SQLException {
        List<Room> ds = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_JOIN + "ORDER BY p.so_phong");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
        }
        return ds;
    }

    public Room layTheoId(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_JOIN + "WHERE p.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? doc(rs) : null;
            }
        }
    }

    // idBoQua: khi sửa, bỏ qua chính phòng đang sửa (thêm mới thì truyền 0)
    public boolean tonTaiSoPhong(String soPhong, int idBoQua) throws SQLException {
        String sql = "SELECT 1 FROM phong WHERE so_phong = ? AND id <> ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, soPhong);
            ps.setInt(2, idBoQua);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void them(Room r) throws SQLException {
        String sql = "INSERT INTO phong (so_phong, tang, trang_thai, loai_phong_id) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            dienThamSo(ps, r);
            ps.executeUpdate();
        }
    }

    public void sua(Room r) throws SQLException {
        String sql = "UPDATE phong SET so_phong=?, tang=?, trang_thai=?, loai_phong_id=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            dienThamSo(ps, r);
            ps.setInt(5, r.getId());
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM phong WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void dienThamSo(PreparedStatement ps, Room r) throws SQLException {
        ps.setString(1, r.getSoPhong());
        if (r.getTang() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, r.getTang());
        ps.setString(3, r.getTrangThai());
        ps.setInt(4, r.getLoaiPhongId());
    }
}