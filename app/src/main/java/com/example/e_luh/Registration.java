package com.example.e_luh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    Button bProceed;
    EditText etUsername, etPassword, etPassword2, etEmail;

    FirebaseDatabase database;
    DatabaseReference reference;
    boolean isAllFieldsChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupActivityLink();

        bProceed = findViewById(R.id.btnRegister);
        etUsername = findViewById(R.id.userName);
        etPassword = findViewById(R.id.password);
        etPassword2 = findViewById(R.id.password2);
        etEmail = findViewById(R.id.email);

        bProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store the returned value of the dedicated function which checks
                // whether the entered data is valid or if any fields are left blank.
                isAllFieldsChecked = CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    String username = etUsername.getText().toString();
                    String password = etPassword2.getText().toString();
                    String email = etEmail.getText().toString();

                    HelperClass helperClass = new HelperClass(username, email, password);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(Registration.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Registration.this, Login.class);
                    startActivity(i);
                }
            }
        });
    }

    private void setupActivityLink() {
        TextView linkTextView = findViewById(R.id.sign_in_link);
        linkTextView.setTextColor(Color.BLUE);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    private boolean CheckAllFields() {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();
        String email = etEmail.getText().toString();


        if (username.length() == 0) {
            etUsername.setError("This field is required");
            return false;
        }

        if (password.length() == 0) {
            etPassword.setError("This field is required");
            return false;
        }

        if (!password2.equals(password)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.length() == 0 || !isEmailValid(email)) {
            etEmail.setError("Invalid email format");
            return false;
        }

        // after all validation return true.
        return true;
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}