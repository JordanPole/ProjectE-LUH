package com.example.e_luh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

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
                    Intent i = new Intent(Login.this, Security.class);
                    startActivity(i);
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
}