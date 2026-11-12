package com.mycompany.hotelmumanage.model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int khachHangId;
    private int phongId;
    private Integer nhanVienId;
    private LocalDateTime ngayDen;
    private LocalDateTime ngayDi;
    private long tienCoc;
    private String trangThai;   // CHO_XAC_NHAN / DA_XAC_NHAN / DA_NHAN_PHONG / DA_TRA_PHONG / HUY
    private String nguon;       // KHACH / LE_TAN
    // 2 trường dưới chỉ để hiển thị danh sách ở STT 22 (lấy qua JOIN)
    private String tenKhach;
    private String soPhong;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getKhachHangId() { return khachHangId; }
    public void setKhachHangId(int khachHangId) { this.khachHangId = khachHangId; }
    public int getPhongId() { return phongId; }
    public void setPhongId(int phongId) { this.phongId = phongId; }
    public Integer getNhanVienId() { return nhanVienId; }
    public void setNhanVienId(Integer nhanVienId) { this.nhanVienId = nhanVienId; }
    public LocalDateTime getNgayDen() { return ngayDen; }
    public void setNgayDen(LocalDateTime ngayDen) { this.ngayDen = ngayDen; }
    public LocalDateTime getNgayDi() { return ngayDi; }
    public void setNgayDi(LocalDateTime ngayDi) { this.ngayDi = ngayDi; }
    public long getTienCoc() { return tienCoc; }
    public void setTienCoc(long tienCoc) { this.tienCoc = tienCoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getNguon() { return nguon; }
    public void setNguon(String nguon) { this.nguon = nguon; }
    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
}