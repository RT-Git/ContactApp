package me.ravitripathi.contactapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<contactDet> contactDetList = new ArrayList<>();
    private RecyclerView recyclerView;
    private contactAdapter adapter;

    // Cursor to load contacts list
    Cursor phones;

    ProgressBar progressBar;

    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent i = new Intent(this, detailsActivity.class);
        progressBar = (ProgressBar) findViewById(R.id.progress);
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_CONTACTS)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
//            }
//        }
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        adapter = new contactAdapter(contactDetList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                contactDet con = contactDetList.get(position);
                i.putExtra("id", con.getId());
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    String[] projection= { "DISTINCT "+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

//                    Cursor address =  getContentResolver().query(android.provider.Telephony.Sms.Inbox.CONTENT_URI,projection, null, null,"address ASC");

                    phones = getContentResolver()
                            .query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    LoadContact loadContact = new LoadContact();
                    loadContact.execute();

                } else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Please grant the contact permission to continue");
                    builder1.setCancelable(true);
                    builder1.setTitle("Access Denied");
                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            String temp ="";
            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                            .parseLong(id));

                    Uri fizz = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    contactDet selectUser = new contactDet(name, phoneNumber);
                    if (fizz != null) {
                        selectUser.setThumbnail(fizz);
                    }


                    selectUser.setId(id);
//                    selectUser.setThumbnail(bit_thumb);
                    if(!temp.equals(name)){
                        contactDetList.add(selectUser);
                    }

                    temp = name;
                }

            } else {
                Log.e("Cursor close 1", "----------------");
            }
            phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new contactAdapter(contactDetList);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Select item on listclick
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    Log.e("search", "here---------------- listener");
//
//                    SelectUser data = selectUsers.get(i);
//                }
//            });
//
//            listView.setFastScrollEnabled(true);
        }
    }


//    public Uri getPhotoUri() {
//        Uri person = ContentUris.withAppendedId(
//                ContactsContract.Contacts.CONTENT_URI, Long.parseLong(getId()));
//        Uri photo = Uri.withAppendedPath(person,
//                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//
//        Cursor cur = this.ctx
//                .getContentResolver()
//                .query(
//                        ContactsContract.Data.CONTENT_URI,
//                        null,
//                        ContactsContract.Data.CONTACT_ID
//                                + "="
//                                + this.getId()
//                                + " AND "
//                                + ContactsContract.Data.MIMETYPE
//                                + "='"
//                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
//                                + "'", null, null);
//        if (cur != null) {
//            if (!cur.moveToFirst()) {
//                return null; // no photo
//            }
//        } else {
//            return null; // error in cursor process
//        }
//        return photo;
//    }

}
