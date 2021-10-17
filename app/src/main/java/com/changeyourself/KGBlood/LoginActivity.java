package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewfoegotpassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        editTextEmail=findViewById(R.id.logInEnterEmail);
        editTextPassword=findViewById(R.id.logInEnterPaswword);
        textViewfoegotpassword=findViewById(R.id.textviewforgotpassword);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void onCklickgobactoSelectregistractivity(View view) {
        Intent intent=new Intent(this,SelectRegistrActivity.class);
        startActivity(intent);
    }

    public void onCklikLigIn(View view) {
        final String email=editTextEmail.getText().toString().trim();
        final String paswword=editTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            Toast.makeText(this, "введите электронную почту", Toast.LENGTH_SHORT).show();
        }
        if(paswword.isEmpty()){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setMessage("Идёт загрузка");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,paswword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Log In is Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}