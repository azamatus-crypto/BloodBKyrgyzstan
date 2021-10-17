package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.UserAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import pojo.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView nav_profile;
    private TextView nav_fullname,nav_email,nav_bloodgroup,nav_age,nav_type;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerview);
        progressBar=findViewById(R.id.progressbar);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList=new ArrayList<>();
        userAdapter=new UserAdapter(userList,this);
        recyclerView.setAdapter(userAdapter);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type=snapshot.child("type").getValue(String.class);
                if(type.equals("Донор")){
                    readRecipients();
                }else{
                    readDonar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle("Nomad Blood Donation");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_profile=navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname=navigationView.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email=navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroup=navigationView.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_age=navigationView.getHeaderView(0).findViewById(R.id.nav_user_age);
        nav_type=navigationView.getHeaderView(0).findViewById(R.id.nav_user_typeofuser);
        navigationView.setNavigationItemSelectedListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    nav_fullname.setText(name);

                    String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    nav_email.setText(email);

                    String bloodgroup = Objects.requireNonNull(snapshot.child("bloodgroup").getValue()).toString();
                    String blF=String.format("Группa крови %s",bloodgroup);
                    nav_bloodgroup.setText(blF);
                    String age = Objects.requireNonNull(snapshot.child("age").getValue()).toString();
                    String bAge=String.format("Bозраст %s",age);
                    nav_age.setText(bAge);

                    String type = Objects.requireNonNull(snapshot.child("type").getValue()).toString();
                    nav_type.setText(type);
                    if(snapshot.hasChild("profilepictureurl")) {
                        String imageUrl = snapshot.child("profilepictureurl").getValue(String.class);
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile);
                    }else{
                        nav_profile.setImageResource(R.drawable.profileimages);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readDonar() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query=databaseReference.orderByChild("type").equalTo("Донор");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "Нету доноров", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readRecipients() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("users");
        Query query=databaseReference.orderByChild("type").equalTo("Получатель");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "Нету получателей", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intentpr=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intentpr);
                break;
            case R.id.logouts:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.compatible:
                Intent compatible= new Intent(MainActivity.this, CategorySelectedActivity.class);
                compatible.putExtra("bloodgr","Группа крови как у меня");
                startActivity(compatible);
                break;
            case R.id.sentemail:
                Intent intentsendEmail = new Intent(MainActivity.this, MailCategoryActivity.class);
                startActivity(intentsendEmail);
                break;
            case R.id.aplus:
                Intent aplusik = new Intent(MainActivity.this, CategorySelectedActivity.class);
                aplusik.putExtra("bloodgr","A+");
                startActivity(aplusik);
                break;
            case R.id.aminus:
                Intent aminusik = new Intent(MainActivity.this, CategorySelectedActivity.class);
                aminusik.putExtra("bloodgr","A-");
                startActivity(aminusik);
                break;
            case R.id.abplus:
                Intent abplusik = new Intent(MainActivity.this, CategorySelectedActivity.class);
                abplusik.putExtra("bloodgr","AB+");
                startActivity(abplusik);
                break;
            case R.id.abminus:
                Intent abminus = new Intent(MainActivity.this, CategorySelectedActivity.class);
                abminus.putExtra("bloodgr","AB-");
                startActivity(abminus);
                break;
            case R.id.oplus:
                Intent oplusik = new Intent(MainActivity.this, CategorySelectedActivity.class);
                oplusik.putExtra("bloodgr","0+");
                startActivity(oplusik);
                break;
            case R.id.ominus:
                Intent ominus = new Intent(MainActivity.this, CategorySelectedActivity.class);
                ominus.putExtra("bloodgr","0-");
                startActivity(ominus);
                break;
            case R.id.notifications:
                Intent intentgdemogno = new Intent(MainActivity.this, WheCanreciveBlood.class);
                startActivity(intentgdemogno);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}