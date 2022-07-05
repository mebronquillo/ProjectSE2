package com.example.loginregis;

import android.widget.TextView;

public class User {

    public String age, email, username, heightCM, weightKG, pass;



    public User(String age, String email, String username, String heightKG, String weightKG, String pass){

        this.age = age;
        this.email = email;
        this.username = username;
        this.heightCM = heightKG;
        this.weightKG = weightKG;
        this.pass = pass;

    }

}
