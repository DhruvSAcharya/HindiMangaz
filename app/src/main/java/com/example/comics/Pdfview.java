package com.example.comics;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class Pdfview extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    WebView webView;
    private boolean loaded = false;
    Button download;
    Intent intent;
    FirebaseFirestore db;
    String downloadlink;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        webView = findViewById(R.id.webview);
        download = findViewById(R.id.pdfvDownloadbtn);
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        //download.setBackgroundColor(Color.GRAY);
        //download.setBackground(R.drawable.icdownload_background);

        getDownloadlink();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "You have already granted this permission!", Toast.LENGTH_SHORT).show();

                } else {
                    requestStoragePermission();
                }

                if(downloadlink.equals("null")){
                    Toast.makeText(Pdfview.this, "Downloading PDF? Not available LOL XD", Toast.LENGTH_SHORT).show();

                }else{
                    //Toast.makeText(Pdfview.this, "in else", Toast.LENGTH_SHORT).show();
                    storeFile();
                }


            }//ONCLICK
        });


        try{

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("File Loading...");
            progressDialog.show();


            String pdf_url = intent.getStringExtra("link");
            //Toast.makeText(this, "link: "+pdf_url, Toast.LENGTH_SHORT).show();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.canZoomIn();
            webView.canZoomOut();
            webView.setClickable(false);
            webView.loadUrl(pdf_url);
            webView.setWebViewClient(new WebViewClient(){

//
            });

            progressDialog.dismiss();

        }catch (Exception e){
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }//on clreate

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Pdfview.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }//REQUESTSTORAHEPERMISSION

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }//ONREQUESTPERMISSION

    private void storeFile() {
        Toast.makeText(this, "In storage file", Toast.LENGTH_SHORT).show();
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(downloadlink));
        String tempTitle=title.replace("","_");
        request.setTitle(title);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title+".pdf");
        DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);

    }//storeFile

    private void getDownloadlink() {

        String subcolname = intent.getStringExtra("subcolname");
        String docname = intent.getStringExtra("documentname");
        title = subcolname+docname.trim();

        DocumentReference docref = db.collection("Mangas").document(subcolname+"")
                .collection(subcolname+"").document(docname+"");
        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.contains("d")) {
                        downloadlink = document.getString("d");
                        //Toast.makeText(Pdfview.this, "download link is field: "+downloadlink, Toast.LENGTH_SHORT).show();


                    } else {
                        downloadlink = "null";
                        Toast.makeText(getApplicationContext(), "Document not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }//getdownloadlink


}