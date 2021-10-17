package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView type, name, email, phoneNumber, bloodGroup,age;
    private CircleImageView profileImage;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar=findViewById(R.id.toolbarprofile);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Мой профиль");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        type = findViewById(R.id.typeuserikadap);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        bloodGroup = findViewById(R.id.bloodGroup);
        age = findViewById(R.id.age);
        profileImage = findViewById(R.id.profileImage);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    type.setText(Objects.requireNonNull(snapshot.child("type").getValue()).toString());
                    name.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    String numberphone=snapshot.child("phonenumber").getValue(String.class);
                    String phoneTotal=String.format("Hомер телефона: %s",numberphone);
                    phoneNumber.setText(phoneTotal);
                    String groupakr=snapshot.child("bloodgroup").getValue(String.class);
                    String gropakrove=String.format("Группa крови: %s",groupakr);
                    bloodGroup.setText(gropakrove);
                    String age1=snapshot.child("age").getValue(String.class);
                    String age2=String.format("Bозраст: %s",age1);
                    age.setText(age2);
                    String email1=snapshot.child("email").getValue(String.class);
                    String email2=String.format("Почтa: %s",email1);
                    email.setText(email2);
                    Glide.with(getApplicationContext()).load(snapshot.child("profilepictureurl").getValue(String.class)).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });


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