package com.mycompany.hotelmumanage.model;

public class Room {
    private int id;
    private String soPhong;
    private Integer tang;        // Integer (không phải int) vì cột tang có thể trống (null)
    private String trangThai;    // TRONG / DANG_O / DANG_DON_DEP / BAO_TRI
    private int loaiPhongId;
    private String tenLoai;      // lấy từ bảng loai_phong qua JOIN, chỉ để hiển thị

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    public Integer getTang() { return tang; }
    public void setTang(Integer tang) { this.tang = tang; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public int getLoaiPhongId() { return loaiPhongId; }
    public void setLoaiPhongId(int loaiPhongId) { this.loaiPhongId = loaiPhongId; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
}