package org.d3ifcool.SistemBelajarSekolah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.SistemBelajarSekolah.Model.DeskripsiSoal;
import org.d3ifcool.SistemBelajarSekolah.Model.Jawaban;
import org.d3ifcool.SistemBelajarSekolah.Model.Soal;
import org.d3ifcool.SistemBelajarSekolah.Model.User;
import org.d3ifcool.SistemBelajarSekolah.Utility.AccountCheck;
import org.d3ifcool.SistemBelajarSekolah.Utility.AdapterDriver;

import java.util.ArrayList;

public class activity_jawab_soal extends AppCompatActivity {

    private ListView listView;
    private Button submitJawaban;

    private ArrayList<Soal> soal = new ArrayList<Soal>();
    private String kodeSoal;
    private User user;
    private AdapterDriver adapterDriver;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jawab_soal);

        listView = (ListView) findViewById(R.id.listViewJawaban);
        submitJawaban = (Button) findViewById(R.id.btn_submitJawaban);


        kodeSoal = getIntent().getStringExtra("kodeSoal");
        mDatabase = FirebaseDatabase.getInstance().getReference("Tugas").child("IsiSoal").child(kodeSoal);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    soal.add(dataSnapshot.getValue(Soal.class));
                    adapterDriver.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapterDriver = new AdapterDriver(this, R.layout.jawaban, soal);
        listView.setAdapter(adapterDriver);

        submitJawaban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setJawabanFirebase();
            }
        });
    }

    private void setJawabanFirebase() throws NullPointerException{
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        user = AccountCheck.user;

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();

        builder.setTitle(R.string.sbm_title_confirm);
        builder.setMessage(R.string.sbm_content);

        builder.setPositiveButton(R.string.sbm_oke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Tugas").child("JawabanMurid").child(user.getId()).child(kodeSoal);
                mDatabase.setValue(adapterDriver.getJawabanList());

                Toast.makeText(getApplicationContext(), R.string.sbm_succes, Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(getApplicationContext(), activity_list_tugas.class));
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

    @Override
    protected void onStart() {
        super.onStart();

        AccountCheck.getAccount(this);
    }
}
