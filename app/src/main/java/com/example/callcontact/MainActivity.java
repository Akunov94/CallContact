package com.example.callcontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity<prefs> extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 101;
    String telNum = null;
    String name = null;
    private TextView textView1;
    private TextView textView2;
    private Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.TxtName);
        textView2 = findViewById(R.id.TxtNumber);
        call = findViewById(R.id.button_Call);
    }

    public void callClick(View view) {
        Intent intentCall=new Intent(Intent.ACTION_CALL);
        if(telNum.trim().isEmpty()){
            intentCall.setData(Uri.parse("tel:0502135151"));
        }
        else{
            intentCall.setData(Uri.parse("tel:"+telNum));
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Please grant permission",Toast.LENGTH_SHORT).show();
            requestPermission();
        }else {
            startActivity(intentCall);
        }

    }
    private  void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                telNum = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                textView1.setText(name);
                textView2.setText(telNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void callClickPick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, RESULT_PICK_CONTACT);
    }
}


