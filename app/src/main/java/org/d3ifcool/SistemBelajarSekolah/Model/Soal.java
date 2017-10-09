package org.d3ifcool.SistemBelajarSekolah.Model;

import java.io.Serializable;

/**
 * Created by Zein Ersyad on 9/11/2017.
 */

public class Soal implements Serializable{
    private int noSoal;
    private String soal;

    public Soal() {
    }

    public Soal(int noSoal, String soal) {
        this.noSoal = noSoal;
        this.soal = soal;
    }

    public void setNoSoal(int noSoal) {
        this.noSoal = noSoal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public int getNoSoal() {
        return noSoal;
    }

    public String getSoal() {
        return soal;
    }
}
