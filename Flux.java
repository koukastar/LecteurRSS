package com.example.user.projet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

public class Flux extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flux);
        Intent ii = getIntent();
        String url=ii.getStringExtra("url");
        webView = findViewById(R.id.webView);
        webView.loadUrl(url);





    }
}
