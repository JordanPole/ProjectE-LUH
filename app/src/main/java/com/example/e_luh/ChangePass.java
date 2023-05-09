package com.example.e_luh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ChangePass extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Button bConfirm, bCancel;
    EditText etConfirmPass, etPassword;
    boolean isAllFieldsChecked = false;



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
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "Change Password" + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));
        // Enable the back button
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        bConfirm = findViewById(R.id.btnConfirm);
        bCancel = findViewById(R.id.btnCancel);
        etConfirmPass = findViewById(R.id.pass2);
        etPassword = findViewById(R.id.pass);


        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store the returned value of the dedicated function which checks
                // whether the entered data is valid or if any fields are left blank.
                isAllFieldsChecked = CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {
                    updatePassword();

                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChangePass.class);
                startActivity(i);
            }
        });

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
                    startActivity(new Intent(ChangePass.this, Report.class));

                }
                else if(item.getItemId() == R.id.nav_security){
                    Log.i("MENU_DRAWER_TAG", "Security Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(ChangePass.this, Security.class));
                }
                else if(item.getItemId() == R.id.nav_about){
                    Log.i("MENU_DRAWER_TAG", "About Us Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(ChangePass.this, AboutUs.class));
                }
                else if(item.getItemId() == R.id.nav_changePass){
                    Log.i("MENU_DRAWER_TAG", "Change Password Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(ChangePass.this, ChangePass.class));
                }
                else if(item.getItemId() == R.id.nav_logout){
                    Log.i("MENU_DRAWER_TAG", "Logout successful");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logout();
                }

                return true;
            }
        });
    }
    public void updatePassword(){
        String user1 = Login.user;
        String userPassword = etPassword.getText().toString().trim();



        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setMessage("Are you sure you want to change your password? This action cannot be undone.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with confirm operation
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(user1);
                        Map<String, Object> updates = new HashMap<String,Object>();
                        updates.put("password", userPassword);

                        ref.updateChildren(updates);

                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ChangePass.this, ChangePass.class);
                        startActivity(i);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private boolean CheckAllFields() {
        String password = etPassword.getText().toString();
        String password2 = etConfirmPass.getText().toString();

        if (etConfirmPass.length() == 0) {
            etConfirmPass.setError("This field is required");
            return false;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("This field is required");
            return false;
        }
        if (!password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // after all validation return true.
        return true;
    }


    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ChangePass.this); //Home is name of the activity
        builder.setMessage("Do you want to LOGOUT?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

//                finish();
                Intent i=new Intent(ChangePass.this, MainActivity.class);
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