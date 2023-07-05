package edu.ptit.saleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.fragment.FragmentChangePassword;
import edu.ptit.saleapp.fragment.FragmentHistory;
import edu.ptit.saleapp.fragment.FragmentHome;
import edu.ptit.saleapp.fragment.FragmentInfo;
import edu.ptit.saleapp.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    protected FirebaseAuth mFirebaseAuth;
    private String email;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        String fullname = getUserFullName(email);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.hello) + " " + fullname);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentHome fragHome = new FragmentHome();
        Bundle bundle = new Bundle();
        bundle.putString("username", email);
        fragHome.setArguments(bundle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                fragHome).addToBackStack(null).commit();

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseAuth.signOut();
                finish();
                break;
            case R.id.nav_search:
                Intent intent=new Intent(MainActivity.this,
                        SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_info:
                sendInfo();
                break;

            case R.id.nav_changPass:
                changPassword();
                break;

            case R.id.nav_history:
                history();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                getSupportFragmentManager().popBackStack();
                super.onBackPressed();
            }
            else
            {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private String getUserFullName(String email)
    {
        SQLiteHelper db = new SQLiteHelper(this);
        User user = db.getUserByEmail(email);
        return user.getFullname();
    }

    private void sendInfo()
    {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        FragmentInfo fragInfo = new FragmentInfo();
        fragInfo.setArguments(bundle);
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                fragInfo).addToBackStack(null).commit();
    }

    private void changPassword() {
//        Intent intent = new Intent(this, ChangePasswordActivity.class);
//        intent.putExtra("email", email);
//        startActivity(intent);

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        FragmentChangePassword fragChangePass = new FragmentChangePassword();
        fragChangePass.setArguments(bundle);
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                fragChangePass).addToBackStack(null).commit();
    }

    private void history()
    {
        FragmentHistory fragmentHistory = new FragmentHistory();
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                fragmentHistory).addToBackStack(null).commit();
    }
}