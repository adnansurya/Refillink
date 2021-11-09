package com.hologram.mks.refillink;

public class histori {

    private String idUser, idHistory, kode_mesin, waktu;
    private double jumlah;
    private long harga;

    public histori(){

    }

    public  String getIdUser(){
        return  idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(String idHistory) {
        this.idHistory = idHistory;
    }

    public String getKode_mesin() {
        return kode_mesin;
    }

    public void setKode_mesin(String kode_mesin) {
        this.kode_mesin = kode_mesin;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }
}
