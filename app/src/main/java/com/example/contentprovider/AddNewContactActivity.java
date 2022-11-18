package com.example.contentprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewContactActivity extends AppCompatActivity {
    EditText editTextNom;
    EditText editTextPrenom;
    EditText editTextNumber;
    Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_contact);

        editTextNom = findViewById(R.id.editTextNom);
        //editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNumber = findViewById(R.id.editTextNumber);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddNewContactActivity.this, Manifest.permission.WRITE_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddNewContactActivity.this,new String []{Manifest.permission.WRITE_CONTACTS},1);
                }
                String name = editTextNom.getText().toString();
                String number = editTextNumber.getText().toString();
                Uri uri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,new ContentValues());
                Long id = ContentUris.parseId(uri);
                ContentValues nameValues = new ContentValues();
                nameValues.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                nameValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name);
                nameValues.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,nameValues);

                ContentValues numberValues = new ContentValues();
                numberValues.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                numberValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER,number);
                numberValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                numberValues.put(ContactsContract.Data.RAW_CONTACT_ID,id);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,numberValues);


                Intent intent = new Intent(AddNewContactActivity.this,ListContactActivity.class);
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(AddNewContactActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}