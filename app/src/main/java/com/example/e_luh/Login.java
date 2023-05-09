package com.example.e_luh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    public static String user;
    Button bProceed, bCancel;
    EditText etUsername, etPassword;
    boolean isAllFieldsChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActivityLink();

        bProceed = findViewById(R.id.btnlogin);
        bCancel = findViewById(R.id.btnCancel);
        etUsername = findViewById(R.id.userName3);
        etPassword = findViewById(R.id.password4);

        bProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store the returned value of the dedicated function which checks
                // whether the entered data is valid or if any fields are left blank.
                isAllFieldsChecked = CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                        checkUser();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupActivityLink() {
        TextView linkTextView = findViewById(R.id.register_link);
        linkTextView.setTextColor(Color.BLUE);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), Registration.class);
                startActivity(intent);
            }
        });
    }

    private boolean CheckAllFields() {

        if (etUsername.length() == 0) {
            etUsername.setError("This field is required");
            return false;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("This field is required");
            return false;
        }


        // after all validation return true.
        return true;
    }

    public void checkUser(){
        String userUsername = etUsername.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("ErrorMessage", "checkUser: he " + userUsername);
                if (snapshot.exists()){
                    user = etUsername.getText().toString();
                    etUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        etUsername.setError(null);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(Login.this, Report.class);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        etPassword.setError("Invalid Credentials");
                        etPassword.requestFocus();
                    }
                } else {
                    etUsername.setError("User does not exist");
                    etUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
