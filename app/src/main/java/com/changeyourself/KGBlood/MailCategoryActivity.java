package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MailCategoryActivity extends AppCompatActivity {
    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private Button buttonsendemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_category);
        editTextTo=findViewById(R.id.edittext_send_to);
        editTextSubject=findViewById(R.id.edittext_subject);
        editTextMessage=findViewById(R.id.edittext_message);
        buttonsendemail=findViewById(R.id.buttonsendmessageemail);
        buttonsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        String recList=editTextTo.getText().toString().trim();
        String[] recipients=recList.split(",");
        String subject=editTextSubject.getText().toString().trim();
        String message=editTextMessage.getText().toString().trim();
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Выберите почту "));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}