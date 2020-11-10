package com.example.comics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



import java.io.File;

public class Pdfview extends AppCompatActivity {
    WebView webView;
    private boolean loaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        webView = findViewById(R.id.webview);



        try{

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("File Loading...");
            progressDialog.show();

            Intent intent = getIntent();
            String pdf_url = intent.getStringExtra("link");
            //Toast.makeText(this, "link: "+pdf_url, Toast.LENGTH_SHORT).show();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.canZoomIn();
            webView.canZoomOut();
            webView.setClickable(false);
            webView.loadUrl(pdf_url);
            webView.setWebViewClient(new WebViewClient(){

//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                    //if(request.getUrl().equals(pdf_url)){
//                        view.loadUrl(pdf_url);
//                    //}
//                   return false;
//                }
//
//                @Override
//                public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//                    view.loadUrl(pdf_url);
//                    return false;
//                }
            });

            progressDialog.dismiss();

        }catch (Exception e){
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }//on clreate


}