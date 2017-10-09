package org.d3ifcool.SistemBelajarSekolah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.SistemBelajarSekolah.Model.DeskripsiSoal;
import org.d3ifcool.SistemBelajarSekolah.Model.Soal;
import org.d3ifcool.SistemBelajarSekolah.Utility.AccountCheck;
import org.d3ifcool.SistemBelajarSekolah.Utility.AdapterDriver;

import java.util.ArrayList;

public class activity_list_tugas extends AppCompatActivity {

    private ArrayList<DeskripsiSoal> listTugas = new ArrayList<DeskripsiSoal>();

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private AdapterDriver adapterDriver;
    private ListView listView;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tugas);

        listView = (ListView) findViewById(R.id.listTugas);

        sharedPreferences = getSharedPreferences(AccountCheck.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Tugas").child("DeskripsiSoal");

        progressDialog = new ProgressDialog(this); //

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
//                    getDataFromServer(dataSnapshot);
                    listTugas.add(dataSnapshot.getValue(DeskripsiSoal.class));
                    adapterDriver.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
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
                progressDialog.dismiss();
            }
        });

        adapterDriver = new AdapterDriver(this, R.layout.tugas, null, listTugas);
        listView.setAdapter(adapterDriver);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(getApplicationContext(), activity_jawab_soal.class)
                .putExtra("kodeSoal", listTugas.get(i).getKodeSoal()));
            }
        });
    }

    public void SignoutMurid(View v){
        editor.clear();
        editor.apply();
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(activity_list_tugas.this, "Sign out Success", Toast.LENGTH_SHORT).show();
                    }
                });
        finish();
        startActivity(new Intent(getApplicationContext(), activity_login.class));
    }
}
