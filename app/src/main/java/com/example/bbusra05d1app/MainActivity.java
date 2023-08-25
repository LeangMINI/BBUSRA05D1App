package com.example.bbusra05d1app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.bbusra05d1app.functions.Sessions;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView nav;
    Sessions sessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayoutID);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.openDrawer,R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = findViewById(R.id.NavigationID);
        nav.setNavigationItemSelectedListener(this);

        // Initialize the Sessions object
        sessions = new Sessions(this);

        // Set user information in navigation header
        View headerView = nav.getHeaderView(0); // Get the header view
        TextView tvHeaderMenuLoginName = headerView.findViewById(R.id.tvHeaderMenuLoginName);
        TextView tvHeaderMenuLoginEmail = headerView.findViewById(R.id.tvHeaderMenuLoginEmail);

        // Get user information from sessions
        String userName = sessions.getUserName();
        String userEmail = sessions.getUserEmail();

        // Set user information in the navigation header
        tvHeaderMenuLoginName.setText(userName);
        tvHeaderMenuLoginEmail.setText(userEmail);

    }
    public void store(View view) {
        Intent add = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(add);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuNewOrder){
            Intent intent = new Intent(MainActivity.this, NewOrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuFavoriteItems) {
            Intent intent = new Intent(MainActivity.this, FavoriteItemsActivity.class);
            startActivity(intent);
        }else if (id == R.id.menuPopularItems) {
            Intent intent = new Intent(MainActivity.this, PopularItemsActivity.class);
            startActivity(intent);
        }else if (id == R.id.menuRecentViews) {
            Intent intent = new Intent(MainActivity.this, RecentViewsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnProducts){
            Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
            startActivity(intent);
        } else if (id == R.id.mnCategories) {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        }
        return true;
    }
}