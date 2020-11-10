package com.example.comics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EpisodCollection extends AppCompatActivity {

    ListView listView;
    FirebaseFirestore db;

    List<String> data;
    ArrayAdapter adapter;
    String subcolname;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episod_collection);

        listView = findViewById(R.id.episodlistview);
        data = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        subcolname = intent.getStringExtra("colname");
        //Toast.makeText(this, "in episod acti oncre: "+subcolname, Toast.LENGTH_SHORT).show();

        getdata();




    }

    public void getdata() {


                db.collection("Mangas").document(subcolname).collection(subcolname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Toast.makeText(SeriesCollection.this, "Data: "+ document.getData(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, document.getId() + " => " + document.getData());
                        String title = document.getString("t");
                        data.add(title);
                        //Toast.makeText(getApplicationContext(), "Title: "+title, Toast.LENGTH_SHORT).show();

                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            String text = listView.getItemAtPosition(position).toString();
                            //Toast.makeText(getApplicationContext(), "text: "+text, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(EpisodCollection.this, "subcolname: "+subcolname, Toast.LENGTH_SHORT).show();
                            DocumentReference docref = db.collection("Mangas").document(subcolname+"").collection(subcolname+"").document(text+"");
                            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        //Toast.makeText(EpisodCollection.this, "data of doc "+document.getData(), Toast.LENGTH_SHORT).show();
                                        if (document != null) {
                                            String link = document.getString("l");

                                            Intent intent = new Intent(getApplicationContext(),Pdfview.class);
                                            intent.putExtra("link",link);
                                            //Toast.makeText(EpisodCollection.this, "link 1: "+link, Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(EpisodCollection.this, "Document not found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(EpisodCollection.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }//oncomplet

                            });


                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }//oncomplet
        });


    }//getdata
}//mainclass