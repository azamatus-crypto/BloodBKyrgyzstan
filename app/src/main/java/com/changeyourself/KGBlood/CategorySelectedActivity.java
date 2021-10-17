package com.changeyourself.KGBlood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.UserAdapter;
import pojo.User;

public class CategorySelectedActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<User> userList;
    private UserAdapter userAdapter;

    private String titleblood = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selected);
        toolbar=findViewById(R.id.toolbarcategoryselected);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.recyclerViewcategory);
        userList=new ArrayList<>();
        userAdapter=new UserAdapter(userList,this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if(getIntent().getExtras()!=null){
            titleblood=getIntent().getStringExtra("bloodgr");
            getSupportActionBar().setTitle("Группа крови "+titleblood);
            if(titleblood.equals("Группа крови как у меня")){
                getCompatibleUsers();
                getSupportActionBar().setTitle("Группа крови как у меня ");
            }else{
                readUsers();
            }
        }
    }

    private void getCompatibleUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type = Objects.requireNonNull(snapshot.child("type").getValue()).toString();
                if (type.equals("Донор")){
                    result = "Получатель";
                }else {
                    result = "Донор";
                }
                String bloodgroup=snapshot.child("bloodgroup").getValue().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("users");
                Query query = reference.orderByChild("search").equalTo(result+" "+bloodgroup);
                query.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result="";
                String type = snapshot.child("type").getValue(String.class);
                if (type.equals("Донор")){
                    result = "Получатель";
                }else {
                    result = "Донор";
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("users");
                Query query = reference.orderByChild("search").equalTo(result+" "+titleblood);
                query.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user = dataSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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