package com.atilsamancioglu.contentprovidercontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = findViewById(R.id.listView);


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_CONTACTS},1);
        }

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                   //Content Provider
                   ContentResolver contentResolver = getContentResolver();
                   String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
                   Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                           projection,
                           null,
                           null,
                           ContactsContract.Contacts.DISPLAY_NAME);

                    if (cursor != null) {

                        ArrayList<String> contactList = new ArrayList<String>();
                        String columnIx= ContactsContract.Contacts.DISPLAY_NAME;

                        while (cursor.moveToNext()) {
                            contactList.add(cursor.getString(cursor.getColumnIndex(columnIx)));
                        }

                        cursor.close();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,contactList);
                        listView.setAdapter(adapter);

                    }


               } else {

                   Snackbar.make(view, "Permission needed", Snackbar.LENGTH_INDEFINITE)
                         .setAction("Give Permission", new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {

                                 if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)) {
                                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                                 } else {
                                     Intent intent = new Intent();
                                     intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                     Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                     intent.setData(uri);
                                     MainActivity.this.startActivity(intent);
                                 }
                             }
                         }).show();
               }

            }
        });
    }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
