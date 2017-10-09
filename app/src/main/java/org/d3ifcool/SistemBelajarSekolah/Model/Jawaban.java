package org.d3ifcool.SistemBelajarSekolah.Model;

/**
 * Created by Zein Ersyad on 9/13/2017.
 */

public class Jawaban extends Soal {

    private String jawaban;
    private int noJawaban;

    public Jawaban(int noSoal, String soal, String jawaban, int noJawaban) {
        super(noSoal, soal);
        this.jawaban = jawaban;
        this.noJawaban = noJawaban;
    }

    public String getJawaban() {
        return jawaban;
    }

    public int getNoJawaban() {
        return noJawaban;
    }
}
