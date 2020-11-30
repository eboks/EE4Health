package com.empatica.sample.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;
import com.empatica.sample.R;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.fragments.SettingsFragment;
import com.empatica.sample.models.Teacher;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EmpaDataDelegate, EmpaStatusDelegate {
    private DrawerLayout drawer;
    Toolbar toolbar;
    RoomDB database;

    //E4connect
    private static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static final String EMPATICA_API_KEY = "574b7f3985804897b373d51a3719229a"; // TODO insert your API Key here
    private EmpaDeviceManager deviceManager = null;

    SettingsFragment fragment;

    private TextView accel_xLabel;
    private TextView accel_yLabel;
    private TextView accel_zLabel;
    private TextView bvpLabel;
    private TextView edaLabel;
    private TextView ibiLabel;
    private TextView temperatureLabel;
    private TextView batteryLabel;
    private TextView statusLabel;
    private TextView deviceNameLabel;
    private LinearLayout dataCnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportFragmentManager().beginTransaction().add(R.id.nav_settings, new SettingsFragment(), "tag").commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        /*SettingsFragment fragment;
        if (savedInstanceState != null) {
            fragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("customtag");
        } else {
            fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.nav_settings, fragment, "customtag").commit();
        }*/

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
        if ( getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
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

    public void initEmpaticaDeviceManager() {
        // Android 6 (API level 23) now require ACCESS_COARSE_LOCATION permission to use BLE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
        } else {

            if (TextUtils.isEmpty(EMPATICA_API_KEY)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Please insert your API KEY")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // without permission exit is the only way
                                //finish();
                            }
                        })
                        .show();
                return;
            }

            // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
            deviceManager = new EmpaDeviceManager(this, this, this);

            // Initialize the Device Manager using your API key. You need to have Internet access at this point.
            deviceManager.authenticateWithAPIKey(EMPATICA_API_KEY);
        }
    }


    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {
        fragment.updateLabel(accel_xLabel, "" + x);
        fragment.updateLabel(accel_yLabel, "" + y);
        fragment.updateLabel(accel_zLabel, "" + z);
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
        fragment.updateLabel(bvpLabel, "" + bvp);
    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        fragment.updateLabel(batteryLabel, String.format("%.0f %%", battery * 100));
    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
        fragment.updateLabel(edaLabel, "" + gsr);
    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {
        fragment.updateLabel(ibiLabel, "" + ibi);
    }

    @Override
    public void didReceiveTemperature(float temp, double timestamp) {
        fragment.updateLabel(temperatureLabel, "" + temp);
    }


    @Override
    public void didReceiveTag(double timestamp) {

    }


    @Override
    public void didEstablishConnection() {

    }


    @Override
    public void didDiscoverDevice(EmpaticaDevice bluetoothDevice, String deviceName, int rssi, boolean allowed) {
        // Check if the discovered device can be used with your API key. If allowed is always false,
        // the device is not linked with your API key. Please check your developer area at
        // https://www.empatica.com/connect/developer.php
        if (allowed) {
            // Stop scanning. The first allowed device will do.
            deviceManager.stopScanning();
            try {
                // Connect to the device
                deviceManager.connectDevice(bluetoothDevice);
                fragment.updateLabel(deviceNameLabel, "To: " + deviceName);
            } catch (ConnectionNotAllowedException e) {
                // This should happen only if you try to connect when allowed == false.
                Toast.makeText(this, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void didFailedScanning(int errorCode) {

    }

    @Override
    public void didRequestEnableBluetooth() {

    }

    @Override
    public void bluetoothStateChanged() {

    }

    @Override
    public void didUpdateOnWristStatus(@EmpaSensorStatus final int status) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (status == EmpaSensorStatus.ON_WRIST) {

                    //((TextView) getView().findViewById(R.id.wrist_status_label)).setText("ON WRIST");
                }
                else {

                    //((TextView) getView().findViewById(R.id.wrist_status_label)).setText("NOT ON WRIST");
                }
            }
        });
    }
    @Override
    public void didUpdateSensorStatus(@EmpaSensorStatus int status, EmpaSensorType type) {

        didUpdateOnWristStatus(status);
    }

    @Override
    public void didUpdateStatus(EmpaStatus status) {
        FragmentManager fm = getSupportFragmentManager();
        SettingsFragment fragment = (SettingsFragment) fm.findFragmentById(R.id.nav_settings);

        // Update the UI
        if(fm == null){
            Log.i("String", "fm");
        }
        if(fragment == null){
            Log.i("String", "fragment");
        }
        if(statusLabel == null){
            Log.i("String", "stat");
        }
        if(status.name() == null){
            Log.i("String", "name");
        }
        fragment.updateLabel(statusLabel, status.name());

        // The device manager is ready for use
        if (status == EmpaStatus.READY) {
            fragment.updateLabel(statusLabel, status.name() + " - Turn on your device");
            // Start scanning
            deviceManager.startScanning();
            // The device manager has established a connection

            hide();

        } else if (status == EmpaStatus.CONNECTED) {

            show();
            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {

            fragment.updateLabel(deviceNameLabel, "");

            hide();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay!
                    initEmpaticaDeviceManager();
                } else {
                    // Permission denied, boo!
                    final boolean needRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    new AlertDialog.Builder(this)
                            .setTitle("Permission required")
                            .setMessage("Without this permission bluetooth low energy devices cannot be found, allow it in order to connect to the device.")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // try again
                                    if (needRationale) {
                                        // the "never ask again" flash is not set, try again with permission request
                                        initEmpaticaDeviceManager();
                                    }
                                    /*else {
                                        // the "never ask again" flag is set so the permission requests is disabled, try open app settings to enable the permission
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }*/
                                }
                            })
                            .setNegativeButton("Exit application", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // without permission exit is the only way
                                    //   finish();
                                }
                            })
                            .show();
                }
                break;
        }
    }

    void show() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                dataCnt.setVisibility(View.VISIBLE);
            }
        });
    }

    void hide() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                dataCnt.setVisibility(View.INVISIBLE);
            }
        });
    }

    public  void initFragmentVar(View view){

        // Initialize vars that reference UI components
        statusLabel = (TextView) view.findViewById(R.id.status);

        dataCnt = (LinearLayout) view.findViewById(R.id.dataArea);

        accel_xLabel = (TextView) view.findViewById(R.id.accel_x);

        accel_yLabel = (TextView) view.findViewById(R.id.accel_y);

        accel_zLabel = (TextView) view.findViewById(R.id.accel_z);

        bvpLabel = (TextView) view.findViewById(R.id.bvp);

        edaLabel = (TextView) view.findViewById(R.id.eda);

        ibiLabel = (TextView) view.findViewById(R.id.ibi);

        temperatureLabel = (TextView) view.findViewById(R.id.temperature);

        batteryLabel = (TextView) view.findViewById(R.id.battery);

        deviceNameLabel = (TextView) view.findViewById(R.id.deviceName);
    }

}
