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
    private TextView headertext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        studentOverviewView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((MainActivity)getActivity()).initFragmentVar(studentOverviewView);

        headertext = (TextView) studentOverviewView.findViewById(R.id.connect_header);
        boolean connected = ((MainActivity)getActivity()).getconnected();
        if(connected){
            updateLabel(headertext,"You are connected to:");
        }
        else{
            updateLabel(headertext,"Available device:");
        }

        SettingsFragment fragment = (SettingsFragment) getParentFragmentManager().getFragments().get(0);
        if(fragment == null){
            Log.i("String", "fragment niet gevonden!");
        }

        Log.i("testtt", "!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        final Button disconnectButton = studentOverviewView.findViewById(R.id.disconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connected = ((MainActivity)getActivity()).getconnected();
                if(connected){
                    ((MainActivity)getActivity()).disconnectDevice();
                }
                else{
                    ((MainActivity)getActivity()).connectDevice();
                }

            }
        });

        ((MainActivity)getActivity()).initEmpaticaDeviceManager(fragment);

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

    public void updatebutton(final Button button, final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setText(text);
            }
        });
    }

    public void hidebutton(final Button button) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.GONE);
            }
        });
    }

    public void showbutton(final Button button) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.VISIBLE);
            }
        });
    }

}


