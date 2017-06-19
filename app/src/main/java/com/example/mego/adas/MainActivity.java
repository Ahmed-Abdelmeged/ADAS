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


package com.example.mego.adas;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.NotAuthEntryActivity;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.auth.VerifyPhoneNumberActivity;
import com.example.mego.adas.sync.AdasSyncUtils;
import com.example.mego.adas.ui.AboutFragment;
import com.example.mego.adas.ui.AccidentFragment;
import com.example.mego.adas.ui.CarFragment;
import com.example.mego.adas.ui.ConnectFragment;
import com.example.mego.adas.ui.DirectionsFragment;
import com.example.mego.adas.ui.HelpFragment;
import com.example.mego.adas.ui.LiveStreamingFragment;
import com.example.mego.adas.ui.SettingsFragment;
import com.example.mego.adas.ui.UserFragment;
import com.example.mego.adas.ui.VideosFragments;
import com.example.mego.adas.user.EditUserInfoActivity;
import com.example.mego.adas.utils.AdasUtils;
import com.example.mego.adas.utils.Communicator;
import com.example.mego.adas.utils.Constant;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.makeText;
import static com.example.mego.adas.utils.Constant.FIREBASE_USERS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Communicator {


    /**
     * UI Element
     */
    private TextView userNameTextView, userEmailTextView;
    private ImageView backgroundImageView, userImageView;
    private Toast toast;


    /**
     * The adapter to get all bluetooth services
     */
    BluetoothAdapter mBluetoothAdapter;

    /**
     * Tag for the log (Debugging)
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * request to enable bluetooth form activity result
     */
    private static final int REQUEST_ENABLE_BT = 1;

    /**
     * the MAC address for the chosen device
     */
    String address = null;

    /**
     * key for the address
     */
    private String address_key = "address_key";

    public static StringBuilder recDataString = new StringBuilder();

    /**
     * Bluetooth services
     */
    private ProgressDialog progressDialog;
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it'
    //This the SPP for the arduino(AVR)
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Handler to get the DataSend from thr thread and drive it to the UI
     */
    public static Handler bluetoothHandler;

    /**
     * used to identify handler message
     */
    final int handlerState = 0;

    public static ConnectedThread mConnectedThread;

    CarFragment carFragment;

    public static boolean connected = false;


    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean isPhoneVerified = false;


    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference, isPhoneAuthDatabaseReference, mUsersImageDatabaseReference;
    private ValueEventListener mUserValueEventListener, isPhoneAuthValueEventListener, mUserImageValueEventListener;
    private ProgressDialog mProgressDialog;
    private int newConnectionFlag = 0;

    private String userImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the MAC address from the Connect Device Activity
        Intent newIntent = getIntent();
        if (savedInstanceState == null) {
            address = newIntent.getStringExtra(ConnectFragment.EXTRA_DEVICE_ADDRESS);
        } else {
            address = savedInstanceState.getString(address_key);
        }
        setContentView(R.layout.activity_main);

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backgroundImageView = (ImageView) findViewById(R.id.start_program_imageView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        initializeScreen(headerView);

        //display current user image
        Bitmap userImageBitmap = AdasUtils.loadUserImageFromStorage(
                AdasUtils.getCurrentUserImagePath(MainActivity.this));
        if (userImageBitmap != null) {
            userImageView.setImageBitmap(userImageBitmap);
        } else {
            userImageView.setImageResource(R.drawable.car_profile_icon);
        }

        openEditAccountActivity();

        //check if the device has a bluetooth or not
        //and show Toast message if it does't have
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, R.string.does_not_have_bluetooth, Toast.LENGTH_SHORT);
            toast.show();

        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntentBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntentBluetooth, REQUEST_ENABLE_BT);
        }

        newConnectionFlag++;

        //check the internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            if (mFirebaseAuth.getCurrentUser().getUid() != null) {
                isPhoneAuthDatabaseReference = mFirebaseDatabase.getReference()
                        .child(FIREBASE_USERS)
                        .child(mFirebaseAuth.getCurrentUser().getUid())
                        .child(Constant.FIREBASE_IS_VERIFIED_PHONE);

                getPhoneVerificationState();
            }
        } else {
            makeText(MainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

        bluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                //if message is what we want
                if (msg.what == handlerState) {
                    // msg.arg1 = bytes from connect thread
                    String readMessage = (String) msg.obj;

                    //keep appending to string until ~ char
                    recDataString.append(readMessage);

                }
            }
        };
        displayUserData();
        verifyUserData();
        AdasSyncUtils.scheduleAdvices(this);
    }


    /**
     * Link the UI Element with XML
     */
    private void initializeScreen(View view) {
        userNameTextView = (TextView) view.findViewById(R.id.header_user_name);

        userEmailTextView = (TextView) view.findViewById(R.id.header_user_email);

        userImageView = (ImageView) view.findViewById(R.id.header_user_imageView);
    }

    /**
     * Method to open edit account activity
     */
    private void openEditAccountActivity() {
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAccountIntent = new Intent(MainActivity.this, EditUserInfoActivity.class);
                startActivity(editAccountIntent);
            }
        });
    }

    /**
     * to disconnect the bluetooth connection
     */
    public void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            if (connected) {
                mConnectedThread.write("d");
            }
            connected = false;

            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Error");
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Disconnect();
        recDataString.delete(0, recDataString.length());
        hideProgressDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (address != null) {
            if (newConnectionFlag == 1) {
                new ConnectBT().execute();
            }
        }
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(address_key, address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_item:
                //sign out from the current user and start the not auth activity
                mFirebaseAuth.signOut();

                //clear the current user information
                AuthenticationUtilities.clearCurrentUser(this);
                AdasUtils.deleteUserImageFromStorage(AdasUtils.getCurrentUserImagePath(this));
                AdasUtils.setCurrentUserImagePath(this, null);

                Intent notAuthIntent = new Intent(MainActivity.this, NotAuthEntryActivity.class);
                startActivity(notAuthIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_videos:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the videos fragment
                VideosFragments videosFragments = new VideosFragments();
                fragmentTransaction.replace(R.id.fragment_container, videosFragments);
                fragmentTransaction.commit();
                break;

            case R.id.nav_settings:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the setting fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.fragment_container, settingsFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_directions:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the directions Fragment
                MapFragment mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.google_map_location);
                if (mapFragment != null) {
                    this.getFragmentManager().beginTransaction().remove(mapFragment).commit();
                }
                DirectionsFragment directionsFragment = new DirectionsFragment();
                fragmentTransaction.replace(R.id.fragment_container, directionsFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_live_streaming:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the live streaming fragment
                LiveStreamingFragment liveStreamingFragment = new LiveStreamingFragment();
                fragmentTransaction.replace(R.id.fragment_container, liveStreamingFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_connect:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the connect fragment
                ConnectFragment connectFragment = new ConnectFragment();
                fragmentTransaction.replace(R.id.fragment_container, connectFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_car:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the car fragment
                MapFragment mapFragmentUser = (MapFragment) this.getFragmentManager().findFragmentById(R.id.my_location_fragment_car);
                if (mapFragmentUser != null) {
                    this.getFragmentManager().beginTransaction().remove(mapFragmentUser).commit();
                }
                carFragment = new CarFragment();
                fragmentTransaction.replace(R.id.fragment_container, carFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_about:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the about fragment
                AboutFragment aboutFragment = new AboutFragment();
                fragmentTransaction.replace(R.id.fragment_container, aboutFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_user:
                MapFragment mapFragmentCar = (MapFragment) this.getFragmentManager().findFragmentById(R.id.my_location_fragment_user);
                if (mapFragmentCar != null) {
                    this.getFragmentManager().beginTransaction().remove(mapFragmentCar).commit();
                }
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the user fragment
                UserFragment userFragment = new UserFragment();
                fragmentTransaction.replace(R.id.fragment_container, userFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_help:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the help fragment
                HelpFragment helpFragment = new HelpFragment();
                fragmentTransaction.replace(R.id.fragment_container, helpFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_accidents:
                backgroundImageView.setVisibility(View.INVISIBLE);
                //setup the accident fragment
                AccidentFragment accidentFragment = new AccidentFragment();
                fragmentTransaction.replace(R.id.fragment_container, accidentFragment);
                fragmentTransaction.commit();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void disconnectListener(long connectionState) {
        CarFragment carFragment = (CarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (carFragment.isAdded() && connected && connectionState == 0) {
            Disconnect();
        }
    }


    /**
     * An AysncTask to connect to Bluetooth socket
     */
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {

            //show a progress dialog in the BluetoothServerActivity
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Connecting...", "Please wait!!!");
        }

        //while the progress dialog is shown, the connection is done in background
        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (btSocket == null || !isBtConnected) {
                    //get the mobile bluetooth device
                    myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    //connects to the device's address and checks if it's available
                    BluetoothDevice bluetoothDevice = myBluetoothAdapter.getRemoteDevice(address);

                    //create a RFCOMM (SPP) connection
                    btSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    //start connection
                    btSocket.connect();
                }

            } catch (IOException e) {
                //if the try failed, you can check the exception here
                connectSuccess = false;
            }

            return null;
        }

        //after the doInBackground, it checks if everything went fine
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!connectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                connected = true;

                isBtConnected = true;
                progressDialog.dismiss();
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();
                mConnectedThread.write("o");
            }

        }
    }

    /**
     * fast way to call Toast
     */
    private void msg(String message) {
        makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * call when the back button is pressed
     */
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //show the user a dialog notifies the user want to close the app
            showLoseConnectionDialog(getResources().getString(R.string.are_you_sure_to_close_the_app));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }*/


    /**
     * create a new class for connect thread
     * to send and read DataSend from the microcontroller
     */
    public class ConnectedThread extends Thread {
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        //Create the connect thread
        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //create I/O stream for the connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;

        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            //keep looping for listen for received message
            while (true) {
                try {
                    //read bytes from input buffer
                    bytes = mmInputStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);

                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothHandler.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            //converted entered string into bytes
            byte[] msgBuffer = input.getBytes();

            //write bytes over bluetooth connection  via outstream
            try {
                mmOutputStream.write(msgBuffer);
            } catch (IOException e) {
                //if you can't write show a Toast message that obtain that
                //Toast.makeText(BluetoothServerActivity.this, "Connection Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Show a dialog that warns the user there are will lose the connection
     * if they continue leaving the app.
     */
    private void showLoseConnectionDialog(
            final String message
    ) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue in the BluetoothServerActivity
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Helper method to show progress dialog
     */
    public void showProgressDialog(String message) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Helper method to hide progress dialog
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * Helper method Verify authentication and display user data
     */
    private void verifyUserData() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();
                    mUsersDatabaseReference = mFirebaseDatabase.getReference().
                            child(FIREBASE_USERS).child(uid);

                    mUsersImageDatabaseReference = mFirebaseDatabase.getReference().child(FIREBASE_USERS)
                            .child(uid).child(Constant.FIREBASE_USER_IMAGE);

                    if (AuthenticationUtilities.isAvailableInternetConnection(getApplicationContext())) {
                        getUserData(uid);
                        if (AdasUtils.getCurrentUserImagePath(MainActivity.this) == null) {
                            getUserImageUrl();
                        }
                    }

                } else {
                    Intent authIntent = new Intent(MainActivity.this, NotAuthEntryActivity.class);
                    startActivity(authIntent);
                    finish();
                }
            }
        };
    }

    /**
     * Method to get the user image url
     */
    private void getUserImageUrl() {
        mUserImageValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userImageUrl = dataSnapshot.getValue(String.class);
                if (userImageUrl != null) {
                    for (int i = 0; i < userImageUrl.length(); i++) {
                        if (userImageUrl.charAt(i) == '?') {
                            userImagePath = userImageUrl.substring(i - 6, i);
                        }
                    }
                    new DownloadUserImageBitmap().execute(userImageUrl);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUsersImageDatabaseReference.addValueEventListener(mUserImageValueEventListener);
    }

    /**
     * AsyncTask to download image
     */
    private class DownloadUserImageBitmap extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                bitmap = Glide.with(MainActivity.this)
                        .load(params[0])
                        .asBitmap()
                        .into(-1, -1)
                        .get();

                if (bitmap != null) {
                    //save the current image
                    String savedPath = AdasUtils.saveImageIntoInternalStorage(bitmap,
                            MainActivity.this, userImagePath);

                    AdasUtils.setCurrentUserImagePath(MainActivity.this
                            , savedPath + "/" + userImagePath);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //display the image after save it
            userImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Helper method to get the data from the database and display it
     */
    private void getUserData(final String uid) {
        mUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User currentUser = dataSnapshot.getValue(User.class);

                    AuthenticationUtilities.setCurrentUser(uid, currentUser.getFullName()
                            , currentUser.getEmail(), currentUser.getPhoneNumber()
                            , currentUser.getLocation(), MainActivity.this);
                    displayUserData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUsersDatabaseReference.addValueEventListener(mUserValueEventListener);
    }

    /**
     * Method to get the current phone verification state
     */
    private void getPhoneVerificationState() {
        isPhoneAuthValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isPhoneVerified = dataSnapshot.getValue(Boolean.class);
                    if (!isPhoneVerified) {
                        hideProgressDialog();
                        Intent mainIntent = new Intent(MainActivity.this, VerifyPhoneNumberActivity.class);
                        //clear the application stack (clear all  former the activities)
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        isPhoneAuthDatabaseReference.addValueEventListener(isPhoneAuthValueEventListener);
    }

    /**
     * Helper method to display user data
     */
    private void displayUserData() {
        User currentUser = AuthenticationUtilities.getCurrentUser(MainActivity.this);

        // check if is a data in the user if not show dialog and wait for the data to load
        if (currentUser.getUserUid() == null) {
            showProgressDialog(getString(R.string.load_user_data));
        } else {
            hideProgressDialog();
        }

        //get the user name
        String name = currentUser.getFullName();
        if (name != null) {
            userNameTextView.setText(name);
        }
        //get the user email
        String email = currentUser.getEmail();
        if (email != null) {
            userEmailTextView.setText(email);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
    }
}
