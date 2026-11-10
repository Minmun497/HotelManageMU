package com.mycompany.hotelmumanage.model;

public class RoomType {
    private int id;
    private String tenLoai;
    private String hinhThuc;   // QUA_DEM / THEO_GIO
    private long donGia;
    private int sucChua;
    private String moTa;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(String hinhThuc) { this.hinhThuc = hinhThuc; }
    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }
    public int getSucChua() { return sucChua; }
    public void setSucChua(int sucChua) { this.sucChua = sucChua; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}