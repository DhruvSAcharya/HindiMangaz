package com.example.comics;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



import java.io.File;

public class Pdfview extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        webView = findViewById(R.id.webview);



        try{
            Intent intent = getIntent();
            String pdf_url = intent.getStringExtra("link");
            Toast.makeText(this, "link: "+pdf_url, Toast.LENGTH_SHORT).show();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(pdf_url);
            webView.setWebViewClient(new WebViewClient());



        }catch (Exception e){
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }//on clreate
}