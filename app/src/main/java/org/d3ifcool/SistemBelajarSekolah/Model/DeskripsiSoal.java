package org.d3ifcool.SistemBelajarSekolah.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zein Ersyad on 9/11/2017.
 */

public class DeskripsiSoal {

    private String kodeSoal;
    private String judul, tipe, waktuKumpul, keteranganTugas;
    private int jumlahSoal;


    public DeskripsiSoal(){}
    public DeskripsiSoal(String judul, String tipe, String waktuKumpul, String keteranganTugas, int jumlahSoal, String kodeSoal) {
        this.judul = judul;
        this.tipe = tipe;
        this.waktuKumpul = waktuKumpul;
        this.keteranganTugas = keteranganTugas;
        this.jumlahSoal = jumlahSoal;
        this.kodeSoal = kodeSoal;
    }

    public String getKodeSoal() {
        return kodeSoal;
    }

    public String getJudul() {
        return judul;
    }

    public String getTipe() {
        return tipe;
    }

    public String getWaktuKumpul() {
        return waktuKumpul;
    }

    public String getKeteranganTugas() {
        return keteranganTugas;
    }

    public int getJumlahSoal() {
        return jumlahSoal;
    }

}
