package com.example.contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ListContactActivity extends AppCompatActivity {
    Button buttonAddContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);
        getPhoneContacts();
        buttonAddContact = findViewById(R.id.buttonAddContact);

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListContactActivity.this, AddNewContactActivity.class);
                startActivity(intent);
            }
        });
    }

    public  void btngetContactPressed(View view){
        getPhoneContacts();
    }


    public void btnAddContact(){

    }
    public void getPhoneContacts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.READ_CONTACTS},0);
        }
        ListView listContacts = findViewById(R.id.listContacts);

        ContentResolver contentResolver = getContentResolver();
        Uri uri  = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null, null,null,null);
        Log.i("CONTACT_PROVIDER_DEMO","TOTAL # of CONTACTS"+Integer.toString(cursor.getCount()));
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("CONTACT_PROVIDER_DEMO","Contact Name :::: "+contactName+"  phone number ::: "+ contactNumber);

            }
        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(ListContactActivity.this,R.layout.list_view_item,cursor,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME.split("1")[0],ContactsContract.CommonDataKinds.Phone.NUMBER},
                new int[]{R.id.textViewName,R.id.textViewNumber},1);
        listContacts.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.app_home){
            Intent intent = new Intent(ListContactActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
