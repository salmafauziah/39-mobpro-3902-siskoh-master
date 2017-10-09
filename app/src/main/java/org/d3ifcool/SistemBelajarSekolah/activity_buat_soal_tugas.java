package org.d3ifcool.SistemBelajarSekolah;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.d3ifcool.SistemBelajarSekolah.Model.DeskripsiSoal;
import org.d3ifcool.SistemBelajarSekolah.Model.Soal;
import org.d3ifcool.SistemBelajarSekolah.Utility.AdapterDriver;
import org.d3ifcool.SistemBelajarSekolah.Utility.DateUtil;

import java.util.ArrayList;
import java.util.Date;

public class activity_buat_soal_tugas extends AppCompatActivity {

    private EditText judulTugas, jumlahSoal, keteranganTugas;
    private RadioButton smartphone, computer, quis;
    private Button datePick, selanjutnya;
    private TextView waktuHitung;
    private LinearLayout deskripsi, isisoal;

    private DeskripsiSoal deskripsiSoal;
    private DatabaseReference mDatabase;
    private AdapterDriver adapterDriver;

    private Button submitSoal;
    private ListView listView;

    private ArrayList<Soal> soalList = new ArrayList<Soal>();

    private String waktuKumpul;
    private int targetHour, targetMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_soal_tugas);

        initiateVariable();

        submitSoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataFirebase();
            }
        });

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeskripsiSoal();
                setIsiSoal();
            }
        });

        quis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick.setText("Waktu Pengumpulan");
            }
        });
        smartphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick.setText("Waktu Pengumpulan");
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick.setText("Tanggal Pengumpulan");
            }
        });
    }


    private void setIsiSoal(){

        int jmlSoal = Integer.parseInt(jumlahSoal.getText().toString());

        for (int i = 0; i < jmlSoal; i++) {
            soalList.add(i, null);
        }

        adapterDriver = new AdapterDriver(this, R.layout.soal, soalList);

        listView.setAdapter(adapterDriver);

    }

    private void setDataFirebase() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();

        builder.setTitle(R.string.sbm_title_confirm);
        builder.setMessage(R.string.sbm_content);
        Log.i("lala", adapterDriver.getSoalList().get(0).getSoal());
        builder.setPositiveButton(R.string.sbm_oke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Tugas").child("DeskripsiSoal");
                String sid = mDatabase.push().getKey();

                deskripsiSoal = new DeskripsiSoal(
                        judulTugas.getText().toString(),
                        getTaskType(),
                        waktuKumpul,
                        keteranganTugas.getText().toString(),
                        Integer.parseInt(jumlahSoal.getText().toString()),
                        sid
                );
                mDatabase.child(sid).setValue(deskripsiSoal);

                mDatabase = FirebaseDatabase.getInstance().getReference("Tugas").child("IsiSoal");
                mDatabase.child(sid).setValue(adapterDriver.getSoalList());

                Toast.makeText(getApplicationContext(), R.string.sbm_succes, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), activity_beranda_guru.class));
            }
        });

        builder.setNegativeButton(R.string.sbm_notOke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void setDeskripsiSoal(){
        deskripsi.setVisibility(View.GONE);
        isisoal.setVisibility(View.VISIBLE);
    }

    private String getTaskType(){
        if(smartphone.isChecked())
            return "Tugas dikerjakan pada Smartphone";
        else if(computer.isChecked())
            return "Tugas dikerjakan pada Komputer";
        else if(quis.isChecked())
            return "Quis";
        return null;
    }

    private void initiateVariable(){
        judulTugas = (EditText) findViewById(R.id.judulTugas);
        jumlahSoal = (EditText) findViewById(R.id.jumlahTugas);
        keteranganTugas = (EditText) findViewById(R.id.keteranganTugas);
        smartphone = (RadioButton) findViewById(R.id.onSmartphone);
        computer = (RadioButton) findViewById(R.id.onComputer);
        quis = (RadioButton) findViewById(R.id.quis);
        datePick = (Button) findViewById(R.id.pickDate);
        selanjutnya = (Button) findViewById(R.id.btn_next);
        waktuHitung = (TextView) findViewById(R.id.tvWaktu);

        deskripsi = (LinearLayout) findViewById(R.id.deskripsiSoal);
        isisoal = (LinearLayout) findViewById(R.id.isiSoal);
        listView = (ListView) findViewById(R.id.listView);
        submitSoal = (Button) findViewById(R.id.btn_submitSoal);
    }

    public void showTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                waktuKumpul = selectedHour + ":" + selectedMinute;
                targetHour = selectedHour;
                targetMinute = selectedMinute;
                datePick.setText(waktuKumpul);
                calculateTime();
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Jam Pengumpulan");
        mTimePicker.show();
    }

    public void showDate(){
        DialogFragment newFragment = new DateUtil() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                super.onDateSet(datePicker, i, i1, i2);
                waktuKumpul = i + "/" + i1 + "/" + i2;
                datePick.setText(waktuKumpul);
            }
        };
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void calculateTime(){
        Date currentTime = Calendar.getInstance().getTime();
        int timeMinute = Math.abs(targetMinute - currentTime.getMinutes()); // masih belum fix, Time different, ga tau gw itungannya..
        int timeHour = targetHour-currentTime.getHours();

        if(timeHour < 0) {
            Toast.makeText(this, "Harap masukan Waktu yang valid", Toast.LENGTH_SHORT).show();
            return;
        }
        waktuHitung.setText(timeHour + " Jam " + timeMinute + " Menit");
    }

    public void pengumpulan(View v){
        if(computer.isChecked()) {
            showDate();
        }else if(quis.isChecked() || smartphone.isChecked()){
            showTime();
        }
    }
}
