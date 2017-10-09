package org.d3ifcool.SistemBelajarSekolah;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.SistemBelajarSekolah.Model.User;
import org.d3ifcool.SistemBelajarSekolah.Utility.AccountCheck;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class activity_login extends AppCompatActivity
    implements  GoogleApiClient.OnConnectionFailedListener{


    private SignInButton SignIn;
    private GoogleSignInOptions signInOptions;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount googleSignInAccount;

    private ImageButton btnTeacher, btnStudent;

    public static final int REQ_CODE = 0;
    public static String idkey;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(AccountCheck.PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setupLoginGoogle();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String id = sharedPreferences.getString("idkey", "");
                if(user != null){
                    setupRegisterToDatabase(id);
                }
            }
        };

        btnStudent = (ImageButton) findViewById(R.id.btnStudent);
        btnTeacher = (ImageButton) findViewById(R.id.btnTeacher);

        SignIn = (SignInButton)findViewById(R.id.bn_login);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,REQ_CODE);
            }
        });

        progressDialog = new ProgressDialog(this); //

        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickRole(1);
            }
        });
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickRole(2);
            }
        });
    }

    private void setupLoginGoogle(){
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();

    }

    private void setupRegisterToDatabase(String id){
        mDatabase = FirebaseDatabase.getInstance().getReference("Account");
        idkey =  id;

        mDatabase.child(idkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    findViewById(R.id.pickRole).setVisibility(View.VISIBLE);
                    findViewById(R.id.signIn).setVisibility(View.GONE);
                }else
                    if(dataSnapshot.getValue(User.class).getTipeUser().equals("Guru")) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), activity_beranda_guru.class));
                    }
                    else if(dataSnapshot.getValue(User.class).getTipeUser().equals("Murid")) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), activity_list_tugas.class));
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void pickRole(int i){
        User user = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        switch (i){
            case 1:
                user = new User(
                        googleSignInAccount.getIdToken(),
                        googleSignInAccount.getId(),
                        googleSignInAccount.getDisplayName(),
                        googleSignInAccount.getEmail(),
                        "Guru",
                        date
                );
                break;
            case 2:
                user = new User(
                        googleSignInAccount.getIdToken(),
                        googleSignInAccount.getId(),
                        googleSignInAccount.getDisplayName(),
                        googleSignInAccount.getEmail(),
                        "Murid",
                        date
                );
                break;
        }
        editor.putString("role", user.getTipeUser());
        editor.commit();
        mDatabase.child(idkey).setValue(user);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Login failed..", Toast.LENGTH_SHORT).show();
    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            googleSignInAccount = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential);

            setupRegisterToDatabase(googleSignInAccount.getId());

            editor.putString("idkey", googleSignInAccount.getId());
            editor.commit();
            Toast.makeText(this, "Berhasil login " + googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Gagal login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE && resultCode == RESULT_OK){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
