package com.example.secroicy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.androidhiddencamera.HiddenCameraFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;


import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private HiddenCameraFragment mHiddenCameraFragment;

    private int CAMREA_CODE = 1;
    private int STORAGE_CODE = 112;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    mTextMessage.setText(R.string.title_home);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startService(new Intent(MainActivity.this, NotificationsMessagingService.class));

//        PushNotifications.start(getApplicationContext(), "410ee95b-fffc-4c01-aaa5-d7760e0358cb");
//        PushNotifications.addDeviceInterest("hello");
//
//        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
//            @Override
//            public void onMessageReceived(RemoteMessage remoteMessage) {
//                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
//                if (messagePayload == null) {
//                    // Message payload was not set for this notification
//                    Log.i("MyActivity", "Payload was missing");
//                } else {
//                    Log.i("MyActivity", messagePayload);
//                    // Now update the UI based on your message payload!
//                }
//            }
//        });

//        PushNotifications.start(
//                getApplicationContext(),
//                "410ee95b-fffc-4c01-aaa5-d7760e0358cb"
//
//        );
//
//        PushNotifications.subscribe("hello");
//
//        PushNotifications.setOnMessageReceivedListener(
//                new PushNotificationReceivedListener() {
//                    @Override
//                    public void onMessageReceived(RemoteMessage remoteMessage) {
//                        String body = remoteMessage.getNotification().getBody();
//                        System.out.println(body);
//                    }
//                }
//        );

        PushNotifications.start(getApplicationContext(), "92aa13be-5600-45b4-9904-62fc7d5927f2");
        PushNotifications.addDeviceInterest("debug-hello");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (permissionAlreadyGranted()) {
            Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
//            return;
        }
        requestPermission();

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) MainActivity.this, PERMISSIONS, STORAGE_CODE );
            } else {
                //do here
                requestPermission();
            }
        } else {
            //do here
            requestPermission();


        }


        findViewById(R.id.btn_using_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mHiddenCameraFragment)
                            .commit();
                    mHiddenCameraFragment = null;
                }

                startService(new Intent(MainActivity.this, HiddenService.class));

                //                finish();
                //                System.exit(0);

            }
        });



//        Pusher code
//        PusherOptions options = new PusherOptions();
//        options.setCluster("mt1");
//
//        Pusher pusher = new Pusher("8fcea27a86c3e8e27515", options);
//
//        pusher.connect(new ConnectionEventListener() {
//            @Override
//            public void onConnectionStateChange(ConnectionStateChange change) {
//                Log.i("Pusher", "State changed from " + change.getPreviousState() +
//                        " to " + change.getCurrentState());
//            }
//
//            @Override
//            public void onError(String message, String code, Exception e) {
//                Log.i("Pusher", "There was a problem connecting! " +
//                        "\ncode: " + code +
//                        "\nmessage: " + message +
//                        "\nException: " + e
//
//                );
////                Toast.makeText(MainActivity.this, "errors ocur puher",Toast.LENGTH_SHORT).show();
//
//            }
//        }, ConnectionState.ALL);
//
//        Channel channel = pusher.subscribe("my-channel");
//    Log.i("Mychannel", "api calls");
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(PusherEvent event) {
//                Log.i("Pusher", "Received event with data: " + event.toString());
//                Toast.makeText(MainActivity.this, "Pusher calls",Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

//    public void next(View v){
//        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//        startActivity(intent);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this,"Logout",Toast.LENGTH_SHORT).show();
            System.exit(0);
            /*   Logout funtionlity cpmment temp     */
        LogOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            return true;
        } else if (id == R.id.action_map_id) {
//            Toast.makeText(MainActivity.this,"Map",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MapActivity.class));
            return true;
        }
        else if (id == R.id.action_maps_id) {
//            Toast.makeText(MainActivity.this,"Map",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        }else { //Kill the activity
            super.onBackPressed();
        }
    }

    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private boolean permissionAlreadyGranted() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storageresult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i("permisson granted", result+"+="+storageresult);
        if (result == PackageManager.PERMISSION_GRANTED && storageresult == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "storage permission", Toast.LENGTH_SHORT).show();
         Log.i("storage permission",result+"<= + => "+storageresult);
            return true;
        }
        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_CODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMREA_CODE);


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMREA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = shouldShowRequestPermissionRationale( Manifest.permission.CAMERA );

                if (! showRationale) {
                    openSettingsDialog();
                }

            }
        }
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = shouldShowRequestPermissionRationale( Manifest.permission.WRITE_EXTERNAL_STORAGE );

                if (! showRationale) {
                    openSettingsDialog();
                }

            }
        }
    }
    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 101);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
//Storage code
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

