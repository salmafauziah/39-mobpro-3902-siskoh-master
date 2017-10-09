package org.d3ifcool.SistemBelajarSekolah.Model;

import java.io.Serializable;

/**
 * Created by Zein Ersyad on 9/10/2017.
 */

public class User implements Serializable{

    public String idToken;
    public String id;
    public String nama;
    public String email;
    public String tipeUser;
    public String created_at;

    public User(){

    }

    public User(String idToken, String id, String nama, String email, String tipeUser, String created_at) {
        this.idToken = idToken;
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.tipeUser = tipeUser;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getIdToken() {
        return idToken;
    }


    public String getTipeUser() {
        return tipeUser;
    }
}
