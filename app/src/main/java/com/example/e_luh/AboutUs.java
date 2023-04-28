package com.example.e_luh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        setupHyperLink();
    }
    private void setupHyperLink() {
        TextView t2 = (TextView) findViewById(R.id.human);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        t2.setText(Html.fromHtml(getString(R.string.hmn)));
    }
}