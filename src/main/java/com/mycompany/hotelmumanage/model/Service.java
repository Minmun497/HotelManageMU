package com.mycompany.hotelmumanage.model;

public class Service {
    private int id;
    private String tenDichVu;
    private String loai;        // PHONG / SPA / PHONG_HOP / DUA_DON
    private long donGia;
    private boolean dangBan;    // true = đang bán, false = ngừng bán

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }
    public boolean isDangBan() { return dangBan; }
    public void setDangBan(boolean dangBan) { this.dangBan = dangBan; }
}