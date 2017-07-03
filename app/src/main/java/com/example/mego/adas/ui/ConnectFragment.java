/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 * Twitter: https://twitter.com/A_K_Abd_Elmeged
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.mego.adas.ui;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mego.adas.application.MainActivity;
import com.example.mego.adas.R;
import com.example.mego.adas.adapter.BluetoothDevicesAdapter;

import java.util.ArrayList;
import java.util.Set;

import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * This Fragment to connect the app with the device(MicroController)
 * if it's a already paired devices using MAC address(Media Access Control)
 * then send the MAC address to the car Fragment as a bundle
 */
public class ConnectFragment extends Fragment {

    /**
     * UI Element
     */
    private FloatingActionButton searchForNewDevices;
    private ListView DevicesList;
    private Toast toast = null;


    /**
     * Return Intent extra (The device MAC address)
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";


    /**
     * The adapter to get all bluetooth services
     */
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    /**
     * request to enable bluetooth form activity result
     */
    public static final int REQUEST_ENABLE_BT = 1;

    ConnectFragment connectFragment;


    /**
     * request to enable bluetooth form activity result
     */
    public static final int REQUEST_ENABLE_FINE_LOCATION = 1256;

    /**
     * Adapter for the devices list
     */
    private BluetoothDevicesAdapter bluetoothDevicesAdapter;
    private ArrayList<String> bluetoothDevicesNamesList = new ArrayList<String>();


    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        initializeScreen(rootView);

        connectFragment = (ConnectFragment) getFragmentManager().findFragmentById(R.id.fragment_container);

        //check if the device has a bluetooth or not
        //and show Toast message if it does't have
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(getContext(), bluetoothDevicesNamesList);

        if (mBluetoothAdapter == null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), R.string.does_not_have_bluetooth, Toast.LENGTH_LONG);
            toast.show();

        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntentBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntentBluetooth, REQUEST_ENABLE_BT);
        } else if (mBluetoothAdapter.isEnabled()) {
            PairedDevicesList();
        }

        if ((mBluetoothAdapter != null)) {
            setBroadCastReceiver();
        }


        //request location permission for bluetooth scanning for android API 23 and above
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ENABLE_FINE_LOCATION);


        //press the button to start search new Devices
        searchForNewDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter != null) {
                    searchForNewDevices.setEnabled(false);
                    bluetoothDevicesAdapter.clear();
                    PairedDevicesList();
                    NewDevicesList();
                } else {
                    makeText(getContext(), R.string.does_not_have_bluetooth, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ENABLE_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted!
                } else {
                    makeText(getContext(), getString(R.string.bluetooth_access_location_required), Toast.LENGTH_LONG).show();
                }
        }
    }

    /**
     * to set the BroadCaster Receiver
     */
    private void setBroadCastReceiver() {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);


        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }


    /**
     * scan for new Devices and pair with them
     */
    private void NewDevicesList() {
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    if (device.getName() != null) {
                        bluetoothDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                searchForNewDevices.setEnabled(true);
                if (pairedDevices.size() == bluetoothDevicesAdapter.getCount()) {
                    makeText(getContext(), getString(R.string.bluetooth_no_device_found), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    /**
     * get the paired devices in the phone
     */
    private void PairedDevicesList() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                //Get the device's name and the address
                bluetoothDevicesAdapter.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            makeText(getContext(), R.string.no_paired_devices,
                    Toast.LENGTH_LONG).show();
        }

        DevicesList.setAdapter(bluetoothDevicesAdapter);
        DevicesList.setOnItemClickListener(bluetoothListClickListener);
    }

    /**
     * handle the click for the list view to get the MAC address
     */
    private AdapterView.OnItemClickListener bluetoothListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            //Get the device MAC address , the last 17 char in the view
            String info = (String) parent.getItemAtPosition(position);
            String MACAddress = info.substring(info.length() - 17);

            Intent connectIntent = new Intent(getContext(), MainActivity.class);
            connectIntent.putExtra(EXTRA_DEVICE_ADDRESS, MACAddress);
            startActivity(connectIntent);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (toast != null) {
            toast.cancel();
        }
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if (connectFragment.isAdded()) {
            // Unregister broadcast listeners
            if (mReceiver.isOrderedBroadcast()) {
                getActivity().unregisterReceiver(mReceiver);
            }
        }
    }


    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen(View view) {
        searchForNewDevices = (FloatingActionButton) view.findViewById(R.id.search_fab_button);
        DevicesList = (ListView) view.findViewById(R.id.devices_list_listView);
    }
}
