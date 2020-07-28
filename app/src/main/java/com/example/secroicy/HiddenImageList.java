package com.example.secroicy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HiddenImageList extends AppCompatActivity {
    private ListView lvPhotos;
    private File[] mPhotos;
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_image_list);

        lvPhotos = (ListView) findViewById(R.id.list_photos);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_settings) {
            updatePhotos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setSupportActionBar(Toolbar toolbar) {
    }


    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhotos != null ? mPhotos.length : 0;
        }

        @Override
        public Object getItem(int i) {
            return mPhotos != null ? mPhotos[i] : mPhotos;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View root = mInflater.inflate(R.layout.item_photo, viewGroup, false);
            ImageView photo = (ImageView) root.findViewById(R.id.photo_image);
            TextView text = (TextView) root.findViewById(R.id.photo_text);
            File f = (File) getItem(i);
            if(f != null) {
                photo.setImageURI(Uri.parse("file://" + f.getPath()));
                String filename = f.getName();
                String label = null;
                filename = filename.replace("secroicyapp_", "");
                filename = filename.replace(".jpeg", "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                try {
                    Date photoDate = sdf.parse(filename);
                    sdf.applyPattern("hh:mm aa\nMMMM, dd, yyyy");
                    label = sdf.format(photoDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text.setText(label != null ? label : filename);
            }
            return root;
        }
    }
    private void updatePhotos() {

        new AsyncTask<Void, Void, Void>() {

            private FilenameFilter mFilter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(".jpg");
                }
            };

            @Override
            protected Void doInBackground(Void... voids) {
                File photosDir = HiddenService.getDir();
                if(photosDir.exists() && photosDir.isDirectory())
                    mPhotos = photosDir.listFiles(mFilter);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PhotoAdapter adapter = new PhotoAdapter();
                lvPhotos.setAdapter(adapter);
            }

        }.execute();

    }
}
