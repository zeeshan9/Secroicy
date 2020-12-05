package com.example.secroicy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.androidhiddencamera.HiddenCameraFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView capture_snapshot,get_snapshot,get_location,upload_image_location,report_lost_phone,post_lost_phone,search_phone,track_location,logout;
    private HiddenCameraFragment mHiddenCameraFragment;
    int PICK_IMAGE_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*capture_snapshot=(ImageView)findViewById(R.id.capture_snapshot);
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
        logout.setOnClickListener(this);*/




    }
    @Override
    public void onClick(View view)
    {
/*
        if(view.getId()==R.id.btn_using_service)
        {
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            */
/*findViewById(R.id.btn_using_service).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mHiddenCameraFragment != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mHiddenCameraFragment)
                                .commit();
                        mHiddenCameraFragment = null;
                    }
                    startService(new Intent(HomeActivity.this, HiddenService.class));
                }
            });*//*

        }
        else if(view.getId()==R.id.get_snapshot)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        }
        else if(view.getId()==R.id.get_location)
        {
            startActivity(new Intent(HomeActivity.this,MapActivity.class));
        }
        else if(view.getId()==R.id.upload_image_location)
        {

        }
        else if(view.getId()==R.id.track_location)
        {
            startActivity(new Intent(HomeActivity.this,TrackLocationActivity.class));
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
            new AlertDialog.Builder(this)
                    .setTitle("Really Logout?")
                    .setMessage("Are you sure you want to logout?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            finish();
                        }
                    }).create().show();
        }
*/
    }
}