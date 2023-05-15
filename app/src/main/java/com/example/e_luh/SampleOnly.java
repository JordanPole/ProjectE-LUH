package com.example.e_luh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class SampleOnly extends AppCompatActivity {
    private Button btnSelect, btnUpload;
    // view for image view
    private ImageView imageView;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    String user1 = Login.user;
    EditText report;
    EditText landmark;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        // Set the title for the ActionBar
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Home" + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));
        // Enable the back button
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_only);

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);
        report = findViewById(R.id.incidentReport);
        landmark = findViewById(R.id.landmark);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    Log.i("MENU_DRAWER_TAG", "Home Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(SampleOnly.this, SampleOnly.class));

                }
                else if(item.getItemId() == R.id.nav_security){
                    Log.i("MENU_DRAWER_TAG", "Security Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(SampleOnly.this, Security.class));
                }
                else if(item.getItemId() == R.id.nav_about){
                    Log.i("MENU_DRAWER_TAG", "About Us Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(SampleOnly.this, AboutUs.class));
                }
                else if(item.getItemId() == R.id.nav_changePass){
                    Log.i("MENU_DRAWER_TAG", "Change Password Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(SampleOnly.this, ChangePass.class));
                }
                else if(item.getItemId() == R.id.nav_logout){
                    Log.i("MENU_DRAWER_TAG", "Logout successful");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logout();
                }

                return true;
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String incident = report.getText().toString();
                String loc = landmark.getText().toString();
                if(incident.equals("")){
                    Toast.makeText(getApplicationContext(), "Please do specify what had happened", Toast.LENGTH_SHORT).show();
                }
                else{
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("reports");
                    Map<String, Object> userReport = new HashMap<>();

                    userReport.put("Username", user1);
                    userReport.put("Details", incident);
                    userReport.put("Location", loc);

                    reference.child("report").push().setValue(userReport);

                    uploadImage();
                }
            }
        });
    }



        // get the Firebase  storage reference


    // Select Image method
    private void SelectImage() {

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

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
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
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(SampleOnly.this,
                                                    "Thank you for submitting an incident report.",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    Intent i = new Intent(SampleOnly.this, Security.class);
                                    startActivity(i);
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(SampleOnly.this,
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
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Submitted "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(SampleOnly.this); //Home is name of the activity
        builder.setMessage("Do you want to log out?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

//                finish();
                Intent i=new Intent(SampleOnly.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                startActivity(i);

            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }

}