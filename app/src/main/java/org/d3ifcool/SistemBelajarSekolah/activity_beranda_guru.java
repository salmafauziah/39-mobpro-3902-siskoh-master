package org.d3ifcool.SistemBelajarSekolah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.SistemBelajarSekolah.Model.User;
import org.d3ifcool.SistemBelajarSekolah.Utility.AccountCheck;


public class activity_beranda_guru extends AppCompatActivity{


    private Button NewQuiz, NewTask, UploadMateri, NilaiMurid, LogoutGuru;
    private TextView namaGuru;

    private User user;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_guru);

        namaGuru = (TextView) findViewById(R.id.namaguru);
        NewQuiz = (Button) findViewById(R.id.btn_newquiz);
        NewTask = (Button) findViewById(R.id.btn_newtask);
        UploadMateri = (Button) findViewById(R.id.btn_uploadmateri);
        NilaiMurid = (Button) findViewById(R.id.btn_nilaimurid);
        LogoutGuru = (Button) findViewById(R.id.logout_guru);

        firebaseAuth = FirebaseAuth.getInstance();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        getAccount();

        NewQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               // startActivity(new Intent(activity_beranda_guru.this, Activity_NewQuiz.class));
            }
        });
        NewTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(activity_beranda_guru.this, activity_buat_soal_tugas.class));
            }
        });
        UploadMateri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // startActivity(new Intent(activity_beranda_guru.this, Activity_UploadMateri.class));
            }
        });
        NilaiMurid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // startActivity(new Intent(activity_beranda_guru.this, Activity_NilaiMurid.class));
            }
        });
        LogoutGuru.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                firebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Toast.makeText(activity_beranda_guru.this, "Sign out Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                finish();
                startActivity(new Intent(getApplicationContext(), activity_login.class));
            }
        });
    }

    public void getAccount(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Account");
        SharedPreferences sharedPreferences = getSharedPreferences(AccountCheck.PREFS_NAME, Context.MODE_PRIVATE);
        String idkey = sharedPreferences.getString("idkey","");

        mDatabase.child(idkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null) {
                    user = dataSnapshot.getValue(User.class);
                    namaGuru.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

