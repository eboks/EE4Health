package com.empatica.sample.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Gaussian;
import com.fuzzylite.term.GaussianProduct;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.term.Sigmoid;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import com.google.android.material.navigation.NavigationView;

import com.fuzzylite.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EmpaDataDelegate, EmpaStatusDelegate {
    //Define parameters of fuzzy logic here!
    float mean_EDA_baseline= (float) 3;
    float stdev_EDA_baseline = (float)2;
    float mean_HR_baseline =(float) 80;
    float stdev_HR_baseline = (float)20;
    float mean_EDA_stress= (float)7;
    float stdev_EDA_stress = (float)1;
    float mean_HR_stress =(float) 120;
    float stdev_HR_stress = (float)20;

    OutputVariable stressLevel;
    InputVariable HR;
    Engine engine;

    private DrawerLayout drawer;
    Toolbar toolbar;
    RoomDB database;

    //E4connect
    private static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static final String EMPATICA_API_KEY = "574b7f3985804897b373d51a3719229a"; // TODO insert your API Key here
    private EmpaDeviceManager deviceManager = null;

    SettingsFragment fragment;

    private TextView deviceNameLabel;
    private TextView headertext;
    private Button button;

    boolean connected = false;
    EmpaticaDevice bluetoothDevice;
    String deviceName;


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

        Log.i("testtt", "before");
        createFuzzyModel();
        Log.i("testtt", "after");

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

    public void initEmpaticaDeviceManager(SettingsFragment frag) {
        fragment = frag;
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
        /*fragment.updateLabel(accel_xLabel, "" + x);
        fragment.updateLabel(accel_yLabel, "" + y);
        fragment.updateLabel(accel_zLabel, "" + z);*/
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
        //fragment.updateLabel(bvpLabel, "" + bvp);
    }

    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
        //fragment.updateLabel(batteryLabel, String.format("%.0f %%", battery * 100));
    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
        //fragment.updateLabel(edaLabel, "" + gsr);
        Log.i("EDA: ", "test");
    }

    @Override
    public void didReceiveIBI(float ibi, double timestamp) {
        //fragment.updateLabel(ibiLabel, "" + ibi);
    }

    @Override
    public void didReceiveTemperature(float temp, double timestamp) {
        //fragment.updateLabel(temperatureLabel, "" + temp);
    }


    @Override
    public void didReceiveTag(double timestamp) {

    }


    @Override
    public void didEstablishConnection() {

    }


    @Override
    public void didDiscoverDevice(EmpaticaDevice bluetoothDevicel, String deviceNamel, int rssi, boolean allowed) {
        bluetoothDevice = bluetoothDevicel;
        deviceName = deviceNamel;
        // Check if the discovered device can be used with your API key. If allowed is always false,
        // the device is not linked with your API key. Please check your developer area at
        // https://www.empatica.com/connect/developer.php
        if (allowed) {
            // Stop scanning. The first allowed device will do.
            deviceManager.stopScanning();
            // Connect to the device
            fragment.updateLabel(deviceNameLabel, deviceName);
            fragment.showbutton(button);
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
        //fragment.updateLabel(statusLabel, status.name());

        // The device manager is ready for use
        if (status == EmpaStatus.READY) {
            //fragment.updateLabel(statusLabel, status.name() + " - Turn on your device");
            // Start scanning
            deviceManager.startScanning();
            // The device manager has established a connection
            fragment.updateLabel(deviceNameLabel, "No device found yet!");
            fragment.updatebutton(button, "Connect");
            fragment.hidebutton(button);

        } else if (status == EmpaStatus.CONNECTED) {

            fragment.updatebutton(button, "Disconnect");
            fragment.updateLabel(headertext, "You are connected to:");
            connected = true;
            fragment.showbutton(button);


            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {

            fragment.updateLabel(headertext, "Available Device:");
            fragment.updateLabel(deviceNameLabel, "No device found yet!");
            fragment.updatebutton(button, "Connect");
            connected = false;
            fragment.hidebutton(button);
            deviceManager.startScanning();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay!
                    initEmpaticaDeviceManager(null);
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
                                        initEmpaticaDeviceManager(null);
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

                //dataCnt.setVisibility(View.VISIBLE);
            }
        });
    }

    void hide() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //dataCnt.setVisibility(View.INVISIBLE);
            }
        });
    }

    public  void initFragmentVar(View view){

        // Initialize vars that reference UI components
       deviceNameLabel = (TextView) view.findViewById(R.id.deviceName);

       headertext = (TextView) view.findViewById(R.id.connect_header);

       button = (Button)view.findViewById(R.id.disconnectButton);
    }

    public void disconnectDevice(){
        if (deviceManager != null) {
            deviceManager.disconnect();
        }
    }

    public void connectDevice(){
        try {
            // Connect to the device
            deviceManager.connectDevice(bluetoothDevice);
            //updateLabel(deviceNameLabel, "To: " + deviceName);
        } catch (ConnectionNotAllowedException e) {
            // This should happen only if you try to connect when allowed == false.
            Toast.makeText(MainActivity.this, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getconnected(){
        return connected;
    }

    public void createFuzzyModel(){
        float mean_EDA_verylow = mean_EDA_baseline - 2*stdev_EDA_baseline;
        float stdev_EDA_verylow = stdev_EDA_baseline;
        float mean_HR_verylow = mean_HR_baseline - 2*stdev_HR_baseline;
        float stdev_HR_verylow = stdev_HR_baseline;

        float mean_EDA_medium = mean_EDA_baseline  + ((mean_EDA_stress) - (mean_EDA_baseline))/2;
        float stdev_EDA_medium = (stdev_EDA_baseline + stdev_EDA_stress)/2;
        float mean_HR_medium = mean_HR_baseline  + ((mean_HR_stress) - (mean_HR_baseline))/2;
        float stdev_HR_medium = (stdev_HR_baseline + stdev_HR_stress)/2;

        float mean_EDA_veryhigh = mean_EDA_stress + 2*stdev_EDA_stress;
        float stdev_EDA_veryhigh = stdev_EDA_stress;
        float mean_HR_veryhigh = mean_HR_stress + 2*stdev_HR_stress;
        float stdev_HR_veryhigh = stdev_HR_stress;


        engine = new Engine();
        engine.setName("StressCalculator");
        engine.setDescription("");

        //Input functions
        //HR
        HR = new InputVariable();
        HR.setName("HR");
        HR.setDescription("");
        HR.setEnabled(true);
        HR.setRange(20.0, 200.0);
        HR.setLockValueInRange(false);

        //HR.addTerm(new GaussianProduct("hvery_low", 0, 0.1, mean_HR_verylow, stdev_HR_verylow));
        HR.addTerm(new Trapezoid("hvery_low", 20, 20.1, mean_HR_verylow, mean_HR_baseline));
        HR.addTerm(new Gaussian("hlow", mean_HR_baseline, stdev_HR_baseline));
        HR.addTerm(new Gaussian("hmedium", mean_HR_medium, stdev_HR_medium));
        HR.addTerm(new Gaussian("hhigh", mean_HR_stress, stdev_HR_stress));
        //HR.addTerm(new GaussianProduct("hvery_high", mean_HR_veryhigh, stdev_HR_veryhigh, 20, 0.1));
        HR.addTerm(new Trapezoid("hvery_high", mean_HR_stress, mean_HR_veryhigh, 200, 200.1));

        engine.addInputVariable(HR);


        //EDA
        InputVariable EDA = new InputVariable();
        EDA.setName("EDA");
        EDA.setDescription("");
        EDA.setRange(0.000, 10);
        EDA.setLockValueInRange(false);

        //EDA.addTerm(new GaussianProduct("very_low", -2, 0.1, mean_EDA_verylow, stdev_EDA_verylow));
        EDA.addTerm(new Trapezoid("very_low", 0, 0.001, mean_EDA_verylow, mean_EDA_baseline));
        EDA.addTerm(new Gaussian("low", mean_EDA_baseline, stdev_EDA_baseline));
        EDA.addTerm(new Gaussian("medium", mean_EDA_medium, stdev_EDA_medium));
        EDA.addTerm(new Gaussian("high", mean_EDA_stress, stdev_EDA_stress));
        //EDA.addTerm(new GaussianProduct("very_high", mean_EDA_veryhigh, stdev_EDA_veryhigh, 20, 0.1));
        EDA.addTerm(new Trapezoid("very_high", mean_EDA_stress, mean_EDA_veryhigh, 20, 20.01));

        engine.addInputVariable(EDA);

        //Output functions
        stressLevel = new OutputVariable();
        stressLevel.setName("stressLevel");
        stressLevel.setDescription("");
        stressLevel.setEnabled(true);
        stressLevel.setRange(0.000, 1);
        stressLevel.setLockValueInRange(false);
        stressLevel.setAggregation(new Maximum());
        stressLevel.setDefuzzifier(new Centroid(200));
        stressLevel.setDefaultValue(Double.NaN);
        stressLevel.setLockPreviousValue(false);

        stressLevel.addTerm(new Triangle("Very_low_stress", 0.0, 0.0, 0.25));
        stressLevel.addTerm(new Triangle("Low_stress", 0.0, 0.25, 0.5));
        stressLevel.addTerm(new Triangle("Medium_stress", 0.25, 0.5, 0.75));
        stressLevel.addTerm(new Triangle("High_stress", 0.5, 0.75, 1.0));
        stressLevel.addTerm(new Triangle("Very_high_stress", 0.75, 1.000, 1.001));

        engine.addOutputVariable(stressLevel);

        //Rules
        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setName("");
        ruleBlock.setDescription("");
        ruleBlock.setEnabled(true);
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setImplication(new Minimum());
        ruleBlock.setActivation(new General());

        ruleBlock.addRule(Rule.parse("if HR is hvery_low and EDA is very_low then stressLevel is Very_low_stress", engine));
        ruleBlock.addRule(Rule.parse("if HR is hlow and EDA is low then stressLevel is Low_stress", engine));
        ruleBlock.addRule(Rule.parse("if HR is hmedium and EDA is medium then stressLevel is Medium_stress", engine));
        ruleBlock.addRule(Rule.parse("if HR is hhigh and EDA is high then stressLevel is High_stress", engine));
        ruleBlock.addRule(Rule.parse("if HR is hvery_high and EDA is very_high then stressLevel is Very_high_stress", engine));

        engine.addRuleBlock(ruleBlock);

        //TESTING
        /*
        for (int i = 0; i <= 50; ++i) {
            float rate = (float) (HR.getMinimum() + i * (HR.range() / 50));
            float skin = (float) (EDA.getMinimum() + i * (EDA.range() / 50));
            HR.setValue(rate);
            EDA.setValue(skin);
            engine.process();
            Log.i("outcome:", "HR = "+Op.str(rate)+" and EDA "+Op.str(skin)+" -> stresslevel = "+Op.str(stressLevel.getValue())+ "");
        }*/
    }

    public void computeStress(){

    }

}
