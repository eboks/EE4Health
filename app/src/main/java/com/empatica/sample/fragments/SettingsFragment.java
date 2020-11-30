package com.empatica.sample.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;
import com.empatica.sample.R;
import com.empatica.sample.activities.MainActivity;
import com.empatica.sample.adapter.ViewPageAdapter;
import com.empatica.sample.models.Student;
import com.google.android.material.tabs.TabLayout;

import static android.content.ContentValues.TAG;

public class SettingsFragment  extends Fragment {

    private View studentOverviewView;
    private static final int REQUEST_ENABLE_BT = 1;

    private EmpaDeviceManager deviceManager = null;

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        studentOverviewView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((MainActivity)getActivity()).initFragmentVar(studentOverviewView);

        final Button disconnectButton = studentOverviewView.findViewById(R.id.disconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }
            }
        });

        ((MainActivity)getActivity()).initEmpaticaDeviceManager();

        return studentOverviewView;

    }

    // Update a label with some text, making sure this is run in the UI thread
    public void updateLabel(final TextView label, final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        });
    }

    /*public  void initVar(View view){
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
    }*/
}


