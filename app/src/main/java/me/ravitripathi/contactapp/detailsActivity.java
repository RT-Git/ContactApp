package me.ravitripathi.contactapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class detailsActivity extends AppCompatActivity {

    private ArrayList<detListItem> list = new ArrayList<>();
    String Grey900 = "#212121";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Slidr.attach(this);

        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(Color.parseColor(Grey900));
//            // Do something for lollipop and above versions
//        }
        ImageView img = (ImageView) findViewById(R.id.prof);
        TextView na = (TextView) findViewById(R.id.name);

        ListView listView = (ListView) findViewById(R.id.listV);
        listViewAdapter listViewAdapter = new listViewAdapter(this, R.layout.det_list_item, list);
        listView.setAdapter(listViewAdapter);

        String id = getIntent().getStringExtra("id");

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}
                , null);

        detListItem emai = new detListItem();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int phonetype = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            String customLabel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
            String phoneLabel = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(this.getResources(), phonetype, customLabel);

            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                    .parseLong(id));

            Uri fizz = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            if (fizz != null) {
                img.setImageURI(fizz);
            }

            if (img.getDrawable() == null) img.setImageResource(R.drawable.ic_white_bot);
            na.setText(name);


            detListItem item = new detListItem();


            item.setTitle(phoneLabel);
            item.setDetails(phoneNumber);
            list.add(item);


//            if(!list.contains(emai)){
//                list.add(emai);
//            }
            listViewAdapter.notifyDataSetChanged();
        }

        cursor.close();


        Cursor emailCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);
        while (emailCur.moveToNext()) {
            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            emai.setTitle("E-mail");
            emai.setDetails(email);
            list.add(emai); // Here you will get list of email
            listViewAdapter.notifyDataSetChanged();

        }
        emailCur.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
