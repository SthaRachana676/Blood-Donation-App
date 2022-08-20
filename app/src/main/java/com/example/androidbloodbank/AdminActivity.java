package com.example.androidbloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.androidbloodbank.Adapter.UserAdapter;
import com.example.androidbloodbank.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private CircleImageView nav_profile;
    private TextView navname, navemail;
    private DatabaseReference userRef;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> userList;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Dashboard");
        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AdminActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        nav_profile = navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        navname = navigationView.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        navemail = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String  name = snapshot.child("name").getValue().toString();
                    navname.setText(name);

                    String  email = snapshot.child("email").getValue().toString();
                    navemail.setText(email);


                    if(snapshot.hasChild("profilepictureurl")){

                        String  imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile);
                    }else {
                        nav_profile.setImageResource(R.drawable.profile);
                    }

//                    Menu nav_menu = nav_view.getMenu();
//                    if (type.equals("recipient")){
//                        nav_menu.findItem(R.id.sentEmail).setTitle("Received Email");
//                        nav_menu.findItem(R.id.notification).setVisible(true);
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.share:
                Intent intent15 = new Intent(AdminActivity.this, ShareActivity.class);
                startActivity(intent15);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;

            case R.id.mngdonor:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(AdminActivity.this, ManagedonorActivity.class);
                startActivity(intent2);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}