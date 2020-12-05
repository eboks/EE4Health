package com.empatica.sample.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.empatica.sample.R;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.fragments.AddStudentFragment;
import com.empatica.sample.fragments.ClassroomFragment;
import com.empatica.sample.fragments.SettingsFragment;
import com.empatica.sample.fragments.TeacherFragment;
import com.empatica.sample.models.Teacher;
import com.google.android.material.navigation.NavigationView;

import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Toolbar toolbar;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        initVar();
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Initialize database
        database = RoomDB.getInstance(this);

        //getCurrentTeacher
        if(getIntent().getExtras() != null){
            Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        }



    }


    private void initVar(){
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onBackPressed() {
        if ( getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
        }
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
