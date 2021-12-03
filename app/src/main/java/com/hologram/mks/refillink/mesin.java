package com.hologram.mks.refillink;

public class mesin {

    private String idMesin, relay, lokasi;
    private double tersedia, kapasitas;

    public mesin(){

    }

    public String getIdMesin() {
        return idMesin;
    }

    public void setIdMesin(String idMesin) {
        this.idMesin = idMesin;
    }

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public double getTersedia() {
        return tersedia;
    }

    public void setTersedia(double tersedia) {
        this.tersedia = tersedia;
    }

    public double getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(double kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
