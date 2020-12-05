package com.example.secroicy;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidhiddencamera.HiddenCameraFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;


import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    Button choose, upload;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String latitude="", longitude="", encodedImg="", time="";
    boolean triggerCheck= false;

    ImageView capture_snapshot,get_snapshot,get_location,upload_image_location,report_lost_phone,post_lost_phone,search_phone,track_location,logout;

    Handler mHandler;
    // views for button
    private Button btnSelect, btnUpload;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
//    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

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
                    startActivity(new Intent(getApplicationContext(), HomeActivity_old.class));
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

        capture_snapshot=(ImageView)findViewById(R.id.btn_using_service);
        get_snapshot=(ImageView)findViewById(R.id.get_snapshot);
        get_location=(ImageView)findViewById(R.id.get_location);
        upload_image_location=(ImageView)findViewById(R.id.upload_image_location);
        track_location=(ImageView)findViewById(R.id.track_location);
        report_lost_phone=(ImageView)findViewById(R.id.report_lost_phone);
        post_lost_phone=(ImageView)findViewById(R.id.post_lost_phone_text);
        search_phone=(ImageView)findViewById(R.id.search_phone);
        logout=(ImageView)findViewById(R.id.logout);

        capture_snapshot.setOnClickListener(this);
        get_snapshot.setOnClickListener(this);
        get_location.setOnClickListener(this);
        upload_image_location.setOnClickListener(this);
        track_location.setOnClickListener(this);
        report_lost_phone.setOnClickListener(this);
        post_lost_phone.setOnClickListener(this);
        search_phone.setOnClickListener(this);
        logout.setOnClickListener(this);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        time = intent.getStringExtra("time");
        triggerCheck = intent.getBooleanExtra("TriggerCheck",false);
        Toast.makeText(this,"TriggerCheck:"+triggerCheck,Toast.LENGTH_LONG).show();



        PushNotifications.start(getApplicationContext(), "410ee95b-fffc-4c01-aaa5-d7760e0358cb");
//        PushNotifications.addDeviceInterest("debug-hello");
        PushNotifications.addDeviceInterest(NetUtils.channelName);

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
                if (messagePayload == null) {
                    Log.i("MyActivity", "Payload was missing");
                    Message message = mHandler.obtainMessage(1, "triggered");
                    message.sendToTarget();
                } else {
                    Log.i("MyActivity", messagePayload);
                    Log.i("MyActivity", "I am here");
                    // Now update the UI based on your message payload!
                }
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (permissionAlreadyGranted()) {
            Toast.makeText(MainActivity.this, "Permission is already granted!", Toast.LENGTH_SHORT).show();
//            return;
        }
        else{

            requestPermission();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) MainActivity.this, PERMISSIONS, STORAGE_CODE );
            }
//            else {
//                //do here
//                requestPermission();
//            }
        } else {
            //do here
            requestPermission();


        }


        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                intent.putExtra("TriggerCheck",true);
                startActivity(intent);
                finish();
            }
        };

        if(triggerCheck){
            Toast.makeText(this,"Location retrieved!", Toast.LENGTH_LONG).show();
            takeHiddenSnapShot();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"Image captured!", Toast.LENGTH_LONG).show();
                    getPictureFromStorage();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"Image Loaded!", Toast.LENGTH_LONG).show();
                    uploadImageLocation();
                }
            }, 1000);
        }

    }

    public void takeHiddenSnapShot(){
        if (mHiddenCameraFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        }

        startService(new Intent(MainActivity.this, HiddenService.class));

    }


    public void getPictureFromStorage(){
        File basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String absolutePath = basePath+ "/SecroicyApp/secroicyapp.jpeg";
        File imgFile = new  File(absolutePath);

        if(imgFile.exists()){
            Bitmap bitmapImg = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            encodedImg = convertBitmapToString(bitmapImg);
        }
    }

    /*              onClick handler           */
    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.btn_using_service)
        {
            takeHiddenSnapShot();
        }
        else if(view.getId()==R.id.get_snapshot)
        {
            getPictureFromStorage();
        }
        else if(view.getId()==R.id.get_location)
        {
            startActivity(new Intent(MainActivity.this,MapActivity.class));
            finish();
        }
        else if(view.getId()==R.id.upload_image_location)
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Send data?")
                    .setMessage("Are you sure you want to send data? Make sure you have captured image and location!")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            uploadImageLocation();
                        }
                    }).create().show();

        }
        else if(view.getId()==R.id.track_location)
        {
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            //startActivity(new Intent(MainActivity.this,TrackLocationActivity.class));
        }
        else if(view.getId()==R.id.report_lost_phone)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetUtils.baseURLClient+"portal/posts/addpost"));
            startActivity(browserIntent);
        }
        else if(view.getId()==R.id.post_lost_phone_text)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetUtils.baseURLClient+"portal/posts"));
            startActivity(browserIntent);
        }
        else if(view.getId()==R.id.search_phone)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetUtils.baseURLClient+"portal/posts"));
            startActivity(browserIntent);
        }
        else if(view.getId()==R.id.logout)
        {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Really Logout?")
                    .setMessage("Are you sure you want to logout?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();
                        }
                    }).create().show();
        }
    }
    /*              --------------------------------------------------------*/

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    public static File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "SecroicyApp");
    }

    // UploadImage method
    private  void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MainActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MainActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"zeeshanmushtaq76@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "testing secroicy");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here from secroicy");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finish sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public  String convertBitmapToString (Bitmap bitmap) {
        ByteArrayOutputStream outputStream =  new  ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat. JPEG ,  70 , outputStream);
        return  Base64.encodeToString (outputStream.toByteArray (), Base64. DEFAULT );
    }


    public void uploadImageLocation(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = NetUtils.baseURServer+"poll/uploaddata";
            JSONObject jsonBody = new JSONObject();


            jsonBody.put("email", NetUtils.email);
            jsonBody.put("latitude", latitude);
            jsonBody.put("longitude", longitude);
            jsonBody.put("time", time);
            jsonBody.put("imageUrl", encodedImg);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        if(responseString.equals("200")){
                            Toast.makeText(MainActivity.this,"data sent successfully", Toast.LENGTH_LONG).show();
                            triggerCheck = false;
                        }
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

