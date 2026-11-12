package com.mycompany.hotelmumanage.model;

public class Customer {
    private int id;
    private String hoTen;
    private String cccd;
    private String sdt;
    private String email;
    private String diaChi;
    private Integer taiKhoanId;   // null = khách chưa có tài khoản đăng nhập

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public Integer getTaiKhoanId() { return taiKhoanId; }
    public void setTaiKhoanId(Integer taiKhoanId) { this.taiKhoanId = taiKhoanId; }
}