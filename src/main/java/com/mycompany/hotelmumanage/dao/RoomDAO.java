package com.mycompany.hotelmumanage.dao;

import com.mycompany.hotelmumanage.model.Room;
import com.mycompany.hotelmumanage.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class RoomDAO {

    private static final String SELECT_JOIN =
    "SELECT p.*, l.ten_loai, l.don_gia FROM phong p JOIN loai_phong l ON p.loai_phong_id = l.id ";

    private Room doc(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setId(rs.getInt("id"));
        r.setSoPhong(rs.getString("so_phong"));
        int tang = rs.getInt("tang");
        r.setTang(rs.wasNull() ? null : tang);
        r.setTrangThai(rs.getString("trang_thai"));
        r.setLoaiPhongId(rs.getInt("loai_phong_id"));
        r.setTenLoai(rs.getString("ten_loai"));
        r.setDonGia(rs.getLong("don_gia"));
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
    
    private String dieuKien(String tuKhoa, String trangThai, int loaiId, List<Object> thamSo) {
    StringBuilder sb = new StringBuilder(" WHERE 1=1");
    if (tuKhoa != null && !tuKhoa.isEmpty()) {
        sb.append(" AND p.so_phong LIKE ?");
        thamSo.add("%" + tuKhoa + "%");
    }
    if (trangThai != null && !trangThai.isEmpty()) {
        sb.append(" AND p.trang_thai = ?");
        thamSo.add(trangThai);
    }
    if (loaiId > 0) {
        sb.append(" AND p.loai_phong_id = ?");
        thamSo.add(loaiId);
        }
    return sb.toString();
    }

public List<Room> timKiem(String tuKhoa, String trangThai, int loaiId, int trang, int soDong)
        throws SQLException {
    List<Object> thamSo = new ArrayList<>();
    String sql = SELECT_JOIN + dieuKien(tuKhoa, trangThai, loaiId, thamSo)
            + " ORDER BY p.so_phong LIMIT ? OFFSET ?";
    thamSo.add(soDong);
    thamSo.add((trang - 1) * soDong);

    List<Room> ds = new ArrayList<>();
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        for (int i = 0; i < thamSo.size(); i++) {
            ps.setObject(i + 1, thamSo.get(i));
        }
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
            }
        }
    return ds;
    }

public int dem(String tuKhoa, String trangThai, int loaiId) throws SQLException {
    List<Object> thamSo = new ArrayList<>();
    String sql = "SELECT COUNT(*) FROM phong p" + dieuKien(tuKhoa, trangThai, loaiId, thamSo);
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        for (int i = 0; i < thamSo.size(); i++) {
            ps.setObject(i + 1, thamSo.get(i));
        }
        try (ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
            }
        }
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
    
    // Tìm phòng qua đêm còn trống trong khoảng [den, di). loaiId = 0 nghĩa là mọi loại.
    public List<Room> timPhongTrong(LocalDateTime den, LocalDateTime di, int loaiId) throws SQLException {
    String sql = SELECT_JOIN
        + "WHERE l.hinh_thuc = 'QUA_DEM' AND p.trang_thai <> 'BAO_TRI' "
        + "AND (? = 0 OR p.loai_phong_id = ?) "
        + "AND NOT EXISTS ( "
        + "    SELECT 1 FROM dat_phong d "
        + "    WHERE d.phong_id = p.id "
        + "    AND d.trang_thai IN ('CHO_XAC_NHAN', 'DA_XAC_NHAN', 'DA_NHAN_PHONG') "
        + "    AND d.ngay_den < ? AND d.ngay_di > ? ) "
        + "ORDER BY p.so_phong";

    List<Room> ds = new ArrayList<>();
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, loaiId);
        ps.setInt(2, loaiId);
        ps.setTimestamp(3, Timestamp.valueOf(di));
        ps.setTimestamp(4, Timestamp.valueOf(den));
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(doc(rs));
        }
    }
    return ds;
    }
}