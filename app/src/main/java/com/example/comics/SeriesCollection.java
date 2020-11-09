package com.example.comics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeriesCollection extends AppCompatActivity {
    ListView listView;
    FirebaseFirestore db;
    DocumentReference documentReference;

    List<String> data;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_collection);

        listView = findViewById(R.id.serieslistview);
        db = FirebaseFirestore.getInstance();

        data = new ArrayList<>();
        //documentReference = db.collection("test").document("test");
        //data.add("abc");
        getdata();

    }//end of oncreate



    public void getdata() {


        try{
            db.collection("Mangas")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Toast.makeText(SeriesCollection.this, "Data: "+ document.getData(), Toast.LENGTH_SHORT).show();
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    String title = document.getString("t");
                                    data.add(title);
                                    Toast.makeText(SeriesCollection.this, "Title: "+title, Toast.LENGTH_SHORT).show();

                                }
                                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String text = listView.getItemAtPosition(position).toString();
                                        Toast.makeText(SeriesCollection.this, "text: "+text, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),EpisodCollection.class);
                                        intent.putExtra("colname",text);
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                Toast.makeText(SeriesCollection.this, "Error", Toast.LENGTH_SHORT).show();
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }//end of gatdata
}