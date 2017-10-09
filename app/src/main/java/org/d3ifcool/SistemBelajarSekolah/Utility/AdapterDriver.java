package org.d3ifcool.SistemBelajarSekolah.Utility;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.d3ifcool.SistemBelajarSekolah.Model.DeskripsiSoal;
import org.d3ifcool.SistemBelajarSekolah.Model.Jawaban;
import org.d3ifcool.SistemBelajarSekolah.Model.Soal;
import org.d3ifcool.SistemBelajarSekolah.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zein Ersyad on 9/11/2017.
 */

public class AdapterDriver extends ArrayAdapter {

    private ArrayList<Soal> soalList = new ArrayList<Soal>();
    private ArrayList<DeskripsiSoal> deskripsiSoalArrayList = new ArrayList<DeskripsiSoal>();
    private ArrayList<Jawaban> jawabanList = new ArrayList<Jawaban>();

    private int resource;

    public AdapterDriver(@NonNull Context context, @LayoutRes int resource, @NonNull List objects, ArrayList<DeskripsiSoal> deskripsiSoalArrayList) {
        super(context, resource, deskripsiSoalArrayList);
        this.deskripsiSoalArrayList = deskripsiSoalArrayList;
        this.resource = resource;
    }

    public AdapterDriver(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Soal> soalList) {
        super(context, resource, soalList);
        this.soalList = soalList;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }


        if(this.resource == R.layout.soal) //jika di activity soal
            ViewSoal(position, convertView, parent);
        else if(this.resource == R.layout.jawaban) //jika di activity jawaban
            ViewJawaban(position, convertView, parent);
        else if(this.resource == R.layout.tugas)
            ViewTugas(position,convertView, parent);

        return convertView;
    }


    private void ViewSoal(final int position, View convertView, ViewGroup parent){
        TextView noSoal = (TextView) convertView.findViewById(R.id.noSoal);
        final EditText isiSoal = (EditText) convertView.findViewById(R.id.isiSoal);

        noSoal.setText((position+1)+"");
        isiSoal.setHint("Masukan Soal Nomor " + (position+1));

        isiSoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                final String _data = editable.toString();
                isiSoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b){
                            soalList.set(position, new Soal(position + 1, _data));
                            // kenapa menggunakan set, ketimbang add
                            // karena kita sudah add untuk setting jumlah kolom soal
                            // gw sampai semaleman nyarinya dan cuman rubah dari add ke set -,-

                            Log.i("Change", _data);
                        }
                    }
                });
            }
        });

    }
    public ArrayList<Soal> getSoalList() {
        return soalList;
    }

    private void ViewJawaban(final int position, View convertView, ViewGroup parent){

        TextView textNo = (TextView) convertView.findViewById(R.id.noJawaban);
        TextView textSoal = (TextView) convertView.findViewById(R.id.textSoalJawaban);
        final EditText editJawab = (EditText) convertView.findViewById(R.id.editJawaban);

        textNo.setText(soalList.get(position).getNoSoal()+"");
        textSoal.setText(soalList.get(position).getSoal()+"");
        editJawab.setHint("Masukan jawaban Nomor " + soalList.get(position).getNoSoal());

        editJawab.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String _data = editable.toString();
                editJawab.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b){
                            jawabanList.add(new Jawaban(soalList.get(position).getNoSoal(),
                                    soalList.get(position).getSoal(),
                                    _data,
                                    soalList.get(position).getNoSoal()));
                            Log.i("Change", _data);
                        }
                    }
                });
            }
        });

    }

    public ArrayList<Jawaban> getJawabanList(){ return jawabanList; }

    private void ViewTugas(final int position, View convertView, ViewGroup parent){

        TextView judulTugas = (TextView) convertView.findViewById(R.id.judulTugas);
        TextView waktuKumpul = (TextView) convertView.findViewById(R.id.waktuKumpul);

        judulTugas.setText(deskripsiSoalArrayList.get(position).getJudul());
        waktuKumpul.setText(deskripsiSoalArrayList.get(position).getWaktuKumpul());
    }
}
