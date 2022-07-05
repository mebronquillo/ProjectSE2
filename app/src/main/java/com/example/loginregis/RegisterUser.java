package com.example.loginregis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser, tvoutput;
    private EditText editTextAge, editTextEmail, editTextPassword, editTextUsername,
            editTextHeightcm, editTextWeightkg;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();


        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);




        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextHeightcm = (EditText) findViewById(R.id.heightCM);
        editTextWeightkg = (EditText) findViewById(R.id.weightKG);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                editTextPassword = (EditText) findViewById(R.id.password);
                tvoutput = (TextView) findViewById(R.id.tvoutput);
                byte[] inputData = editTextPassword.getText().toString().getBytes();
                byte[] outputData = new byte[0];
                try{
                    outputData = sha.encryptSHA(inputData, "SHA-1");
                } catch (Exception e){
                    e.printStackTrace();
                }
                BigInteger shaData = new BigInteger(1, outputData);
                tvoutput.setText(shaData.toString(16));
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        String age = editTextAge.getText().toString().trim();
        String heightCM = editTextHeightcm.getText().toString().trim();
        String weightKG = editTextWeightkg.getText().toString().trim();
        String pass = tvoutput.getText().toString().trim();




        if(heightCM.isEmpty()){
            editTextHeightcm.setError("Height in cm is required!");
            editTextHeightcm.requestFocus();
            return;
        }

        if(weightKG.isEmpty()){
            editTextWeightkg.setError("Weight in kg is required!");
            editTextWeightkg.requestFocus();
            return;
        }

        if(age.isEmpty()){
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid Email!");
            editTextEmail.requestFocus();
            return;
        }

        if(username.isEmpty()){
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 10){
            editTextPassword.setError("Min password length should be 10 character!");
            editTextPassword.requestFocus();
            return;
            
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(age, email, username, heightCM, weightKG, pass);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this, "User has been registered success", Toast.LENGTH_LONG).show();


                                            }else{
                                                Toast.makeText(RegisterUser.this, "Failed to register! Please Try again!", Toast.LENGTH_LONG).show();


                                            }
                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register! Please Try again! ito?", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }




}