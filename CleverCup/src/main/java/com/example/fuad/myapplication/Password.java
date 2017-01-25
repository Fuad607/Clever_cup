package com.example.fuad.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Password extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private FirebaseAuth firebaseAuth;
    private Button buttonpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordd);
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail=(EditText) findViewById(R.id.editResetEmail);
        buttonpassword=(Button) findViewById(R.id.buttonresetpassword);
        buttonpassword.setOnClickListener(this);
    }



    private void ResetPass() {

        String email=editTextEmail.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;

        }
        if(Connection()==false){
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email);
        Toast.makeText(this,"A password reset email is sent ",Toast.LENGTH_SHORT).show();
            onBackPressed();
    }
    public boolean Connection(){
        ConnectivityManager connect= (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connect.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected()){
            return true;

        }return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonresetpassword){
            ResetPass();
        }
    }


}
