package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorRegistrationActivity extends AppCompatActivity {
    private CircleImageView circleImageViewdonorprofile;
    private EditText editTextfullname;
    private EditText editTextIdnumberage;
    private EditText editTextphonenumber;
    private EditText editTextemail;
    private EditText editTextpassword;
    private Spinner spinnerchoosebloodtype;
    private Uri uri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);
        circleImageViewdonorprofile = findViewById(R.id.profile_imagedonorregistration);
        editTextfullname = findViewById(R.id.donorregistrenterfullname);
        editTextIdnumberage = findViewById(R.id.donorregistrenterEnteridnumberage);
        editTextphonenumber = findViewById(R.id.donorregistrenterEnterphonenumber);
        editTextemail = findViewById(R.id.donorregistrenterEnterEmail);
        editTextpassword = findViewById(R.id.donorregistrenterEnterPaswword);
        spinnerchoosebloodtype = findViewById(R.id.bloodGroupsSpinner);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void onClickRegistrDonor(View view) {
        final String email = editTextemail.getText().toString().trim();
        final String password = editTextpassword.getText().toString().trim();
        final String fullname = editTextfullname.getText().toString().trim();
        final String phonenumber = editTextphonenumber.getText().toString().trim();
        final String bloodgroup = spinnerchoosebloodtype.getSelectedItem().toString();
        final String age = editTextIdnumberage.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "введите электронную почту", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phonenumber.isEmpty()) {
            Toast.makeText(this, "введите номер телефона", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fullname.isEmpty()) {
            Toast.makeText(this, "введите имя и Фамилию", Toast.LENGTH_SHORT).show();
            return;
        }
        if (age.isEmpty()) {
            Toast.makeText(this, "Введите возраст", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bloodgroup.equals("выберите группу крови")) {
            Toast.makeText(this, "Ошибка выберите группу крови", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "Идёт регистрация", Toast.LENGTH_SHORT).show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        String error = task.getException().toString();
                        Toast.makeText(DonorRegistrationActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else {
                        String currentuserID = firebaseAuth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(currentuserID);
                        HashMap userinfo = new HashMap();
                        userinfo.put("id", currentuserID);
                        userinfo.put("name", fullname);
                        userinfo.put("email", email);
                        userinfo.put("age", age);
                        userinfo.put("phonenumber", phonenumber);
                        userinfo.put("bloodgroup", bloodgroup);
                        userinfo.put("type", "Донор");
                        userinfo.put("search", "Донор " + bloodgroup);
                        databaseReference.updateChildren(userinfo).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DonorRegistrationActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DonorRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        });
                        //adding image to firebase
                        if (uri != null) {
                            final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                    .child("profile images").child(currentuserID);
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            assert bitmap != null;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                            byte[] data = byteArrayOutputStream.toByteArray();
                            UploadTask uploadTask = filePath.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DonorRegistrationActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                Map newImageMap = new HashMap();
                                                newImageMap.put("profilepictureurl", imageUrl);
                                                databaseReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(DonorRegistrationActivity.this, "added url image to database", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(DonorRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });
                            Intent intent = new Intent(DonorRegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
    }

    public void onCklickgobactoLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onCklickAddPhoto(View view) {
        //vibiraet phot iz gallereii N1
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override//N2
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            circleImageViewdonorprofile.setImageURI(uri);
        }
    }
}