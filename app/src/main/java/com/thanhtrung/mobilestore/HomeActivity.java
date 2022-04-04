package com.thanhtrung.mobilestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thanhtrung.mobilestore.databinding.ActivityHomeBinding;
import com.thanhtrung.mobilestore.models.UserModel;
import com.thanhtrung.mobilestore.ui.category.CategoryFragment;
import com.thanhtrung.mobilestore.ui.home.HomeFragment;
import com.thanhtrung.mobilestore.ui.profile.ProfileFragment;

import java.util.concurrent.atomic.AtomicMarkableReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_category,R.id.nav_offers,R.id.nav_new_products
                ,R.id.nav_my_orders,R.id.nav_my_carts)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        database = FirebaseDatabase.getInstance();
        View headerView = navigationView.getHeaderView(0);

        TextView headerName = headerView.findViewById(R.id.nav_header_name);
        TextView headerEmail = headerView.findViewById(R.id.nav_header_email);
        CircleImageView headerImg = headerView.findViewById(R.id.nav_header_img);
        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        headerName.setText(userModel.getName());
                        headerEmail.setText(userModel.getEmail());
                        Glide.with(HomeActivity.this).load(userModel.getProfileImg()).into(headerImg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new HomeFragment()).commit();
                        break;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new ProfileFragment()).commit();
                        break;
                    case R.id.nav_category:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new CategoryFragment()).commit();
                        break;
                    case R.id.nav_new_products:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new NewProductsFragment()).commit();
                        break;
                    case R.id.nav_my_orders:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new MyOrdersFragment()).commit();
                        break;
                    case R.id.nav_my_carts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,new MyCartsFragment()).commit();
                        break;
                    case R.id.nav_offers:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(HomeActivity.this, "Sig Out successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}