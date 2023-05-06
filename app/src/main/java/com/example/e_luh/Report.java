package com.example.e_luh;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Report extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    //Initialize variable
    Button btPick;
    RecyclerView recyclerView;

    Button proceed;
    EditText report;

    ArrayList<Uri> arrayList = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();

        // Set the title for the ActionBar
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Homepage" + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));
        // Enable the back button
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);



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
                    startActivity(new Intent(Report.this, Report.class));

                }
                else if(item.getItemId() == R.id.nav_security){
                    Log.i("MENU_DRAWER_TAG", "Security Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Report.this, Security.class));
                }
                else if(item.getItemId() == R.id.nav_about) {
                    Log.i("MENU_DRAWER_TAG", "About Us Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Report.this, AboutUs.class));
                }
                else if(item.getItemId() == R.id.nav_changePass){
                    Log.i("MENU_DRAWER_TAG", "Change Password Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Report.this, ChangePass.class));
                }
                else if(item.getItemId() == R.id.nav_logout){
                    Log.i("MENU_DRAWER_TAG", "Logout is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logout();

                }

                return true;
            }
        });

        //Assign Variable
        btPick = findViewById(R.id.bt_pick);
        recyclerView = findViewById(R.id.recycler_view);
        proceed = findViewById(R.id.submit);
        report = findViewById(R.id.incidentReport);

        //Set listener on button
        btPick.setOnClickListener(view -> {
            //Define camera & storage permissions
            String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            //Check condition
            if (EasyPermissions.hasPermissions(this, strings)){
                //When permissions are already granted
                //Create method
                imagePicker();
            } else {
                //When permission is not granted
                //Request permission
                EasyPermissions.requestPermissions(
                        this,
                        "App needs to access to your camera and storage",
                        100,
                        strings
                );
            }
        });

        proceed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String incident = report.getText().toString();

                if(incident.equals("")){
                    Toast.makeText(getApplicationContext(), "Please do specify what had happened", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(Report.this, Security.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(resultCode == RESULT_OK && data != null){
            //When condition contain data
            //Check condition
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                //When request for photo
                //Initialize array list
                arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                //Set layout manager
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                //Set Adapter
                recyclerView.setAdapter(new MainAdp(arrayList));
            }
        }
    }

    private void imagePicker() {
        //Open picker
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(4)
                .setSelectedFiles(arrayList)
                .pickPhoto(this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //Check condition
        if(requestCode == 100 && perms.size() == 2){
            //When permissions are granted
            //Call method
            imagePicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //Check condition
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            //When permissions are denied multiple times
            //Open app settings
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            //When permission deny once
            //Display toast
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Report.this); //Home is name of the activity
        builder.setMessage("Do you want to LOGOUT?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

//                finish();
                Intent i=new Intent(Report.this, MainActivity.class);
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


        return;
    }

}
