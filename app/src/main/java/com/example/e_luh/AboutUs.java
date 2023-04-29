package com.example.e_luh;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutUs extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

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
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "About Us" + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006400")));
        // Enable the back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        setupHyperLink();

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
                    startActivity(new Intent(AboutUs.this, Login.class));
                }
                else if(item.getItemId() == R.id.nav_security){
                Log.i("MENU_DRAWER_TAG", "Your Security Item is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(AboutUs.this, Security.class));
            }
                else if(item.getItemId() == R.id.nav_about) {
                    Log.i("MENU_DRAWER_TAG", "About Us Item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AboutUs.this, AboutUs.class));
                }
                else if(item.getItemId() == R.id.nav_changePass){
                Log.i("MENU_DRAWER_TAG", "Change Password Item is clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(AboutUs.this, ChangePass.class));
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
    private void setupHyperLink() {
        TextView t2 = (TextView) findViewById(R.id.human);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        t2.setText(Html.fromHtml(getString(R.string.hmn)));
    }
    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AboutUs.this); //Home is name of the activity
        builder.setMessage("Do you want to LOGOUT?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

//                finish();
                Intent i=new Intent(AboutUs.this, MainActivity.class);
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