package com.example.meettime;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meettime.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);

        // Set the navigation item listener
        navView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else if (itemId == R.id.navigation_settings) {
                    Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingIntent);
                } else if (itemId == R.id.navigation_notifications) {
                    Intent notificationIntent = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(notificationIntent);
                } else if (itemId == R.id.navigation_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(MainActivity.this, RegisterationActivity.class);
                    startActivity(logoutIntent);
                    finish();
                }
                return true;
            };

}
