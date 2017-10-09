package org.d3ifcool.SistemBelajarSekolah.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.d3ifcool.SistemBelajarSekolah.Model.User;
import org.d3ifcool.SistemBelajarSekolah.activity_login;

/**
 * Created by Zein Ersyad on 10/1/2017.
 */

public class AccountCheck {

    public static final String PREFS_NAME = "LoginPrefs";
    public static User user;
    private static DatabaseReference mDatabase;

    public static void getAccount(Context mContext){
        mDatabase = FirebaseDatabase.getInstance().getReference("Account");
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String idkey = sharedPreferences.getString("idkey","");

        mDatabase.child(idkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               user = dataSnapshot.getValue(User.class);

                Log.i("lala", idkey);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
