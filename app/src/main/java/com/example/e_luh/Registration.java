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

public class Registration extends AppCompatActivity {

    Button bProceed;
    EditText etUsername, etPassword, etPassword2, etEmail;
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
            etPassword2.setError("Passwords do not match");
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