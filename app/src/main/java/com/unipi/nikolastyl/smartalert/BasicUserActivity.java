package com.unipi.nikolastyl.smartalert;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class BasicUserActivity extends AppCompatActivity implements LocationListener {

    ImageView imageView;
    EditText description;
    TextView timestamp;
    Spinner category;
    Date ts1;
    FirebaseDatabase database;
    DatabaseReference dataRef;
    private Uri filePath;
    StorageReference ref;
    LocationManager locationManager;
    String currLoc,filepath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_user);
        imageView=findViewById(R.id.imageView);
        description=findViewById(R.id.editTextDescription);
        timestamp=findViewById(R.id.textView5);
        category=findViewById(R.id.spinner);
        ts1 = Calendar.getInstance().getTime();
        timestamp.setText(ts1.toString());
        database=FirebaseDatabase.getInstance();
        dataRef=database.getReference();
        ref=FirebaseStorage.getInstance().getReference();
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        // spinner default list
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("fire");
        arrayList.add("flood");
        arrayList.add("high temperatures");
        arrayList.add("snow");
        arrayList.add("landslide");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + selected, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        //locationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);


    }

    public void uploadPhoto(View v){


        int requestExternalStorage = 13123;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES},
        requestExternalStorage);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent iGalery = new Intent(Intent.ACTION_PICK);
            iGalery.setType("image/*");
            startActivityForResult(iGalery,2);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
        imageView.setImageURI(data.getData());
            // Get the Uri of data
            filePath = data.getData();

    }

    public void report(View v){
        if(filePath!=null) { //if file selected, save it in the firebase storage and save the fileUrl in the realtime database

            StorageReference storageReference = ref.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            storageReference.putFile(filePath);
            storageReference.getDownloadUrl();
            filepath2= filePath.toString();
        }else{
            filepath2="---";//if file is null, save --- in the realtime database

        }


        HashMap<String,Object> hashMap=new HashMap<>();//hash map for the realtime database
        hashMap.put("timestamp",ts1.toString());
        hashMap.put("location",currLoc);
        hashMap.put("category",category.getSelectedItem().toString());
        hashMap.put("description",description.getText().toString());
        hashMap.put("photo",filepath2);


        dataRef.child("reports")
                .child(ts1.toString())
                .setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(BasicUserActivity.this, "thanks for the report", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BasicUserActivity.this, BasicUserActivity.class));

                        }else{
                            Toast.makeText(BasicUserActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }





    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        currLoc=location.getLatitude()+","+location.getLongitude();

    }
}