package com.example.fuad.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText rePassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView textview;
    private DatabaseReference mdatabaseReference;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        buttonRegister=(Button) findViewById(R.id.buttonRegister);
        editTextEmail =(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editPassword);
        rePassword=(EditText) findViewById(R.id.rePassword);
        textview=(TextView) findViewById(R.id.textviewsignin);

        mdatabaseReference= FirebaseDatabase.getInstance().getReference();

        buttonRegister.setOnClickListener(this);
        textview.setOnClickListener(this);


    }
    private void registerUser(){

        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String repassword=rePassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!repassword.equals(password)){
            Toast.makeText(this,"Password is not matching" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if(Connection()==false){
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user=firebaseAuth.getCurrentUser();
                            mdatabaseReference.child(user.getUid()).setValue("0");
                            Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else{
                            Toast.makeText(Register.this,"Registration unseccessfully,Please try again",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    public boolean Connection() {
        ConnectivityManager connect = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view==buttonRegister){
            registerUser();
        }
        if(view == textview){
            onBackPressed();
        }

    }
}
