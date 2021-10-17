package com.changeyourself.KGBlood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectRegistrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registr);
    }

    public void onCklickgobactoLoginActivity(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void onClickgotoRecipientRegistActivity(View view) {
        Intent intent=new Intent(this,RecipientActivity.class);
        startActivity(intent);
    }

    public void onClickgotoDonorRegistActivity(View view) {
        Intent intent=new Intent(this,DonorRegistrationActivity.class);
        startActivity(intent);
    }
}