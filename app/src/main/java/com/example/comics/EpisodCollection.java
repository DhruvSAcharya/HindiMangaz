package com.example.comics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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

    RewardedAd rewardedAd;
    Boolean adreward=false;

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
        //inisialization
        listView = findViewById(R.id.episodlistview);
        data = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-7754027268475201/1323334156");



        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Toast.makeText(EpisodCollection.this, "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                Toast.makeText(EpisodCollection.this, "Ad failed to load.", Toast.LENGTH_SHORT).show();
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);





        Intent intent = getIntent();
        subcolname = intent.getStringExtra("colname");
        //Toast.makeText(this, "in episod acti oncre: "+subcolname, Toast.LENGTH_SHORT).show();

        getdata();




    }//oncreate

    @Override
    protected void onStart() {
        super.onStart();

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-7754027268475201/1323334156");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Toast.makeText(EpisodCollection.this, "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                Toast.makeText(EpisodCollection.this, "Ad failed to load.", Toast.LENGTH_SHORT).show();
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

    }//onstart

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
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            TextView tv = (TextView) view.findViewById(android.R.id.text1);

                            // Set the text size 25 dip for ListView each item
                            //tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                            tv.setTextColor(Color.WHITE);
                            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

                            // Return the view
                            return view;

                        }
                    };
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                            if (rewardedAd.isLoaded()) {

                                Activity activityContext = EpisodCollection.this;
                                RewardedAdCallback adCallback = new RewardedAdCallback() {
                                    @Override
                                    public void onRewardedAdOpened() {
                                        // Ad opened.
                                    }

                                    @Override
                                    public void onRewardedAdClosed() {
                                        // Ad closed.
                                        if (adreward) {

                                            //start
                                            String docname = listView.getItemAtPosition(position).toString();
                                            //Toast.makeText(getApplicationContext(), "text: "+text, Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(EpisodCollection.this, "subcolname: "+subcolname, Toast.LENGTH_SHORT).show();
                                            DocumentReference docref = db.collection("Mangas").document(subcolname+"")
                                                    .collection(subcolname+"").document(docname+"");
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
                                                            intent.putExtra("subcolname",subcolname);
                                                            intent.putExtra("documentname",docname);
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
                                            //end


                                        }//end if



                                    }//onclose

                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                                        Toast.makeText(activityContext, "Now, you can close the AD", Toast.LENGTH_SHORT).show();
                                        adreward = true;
                                        // User earned reward.
                                    }//onuserEarbedrewared

                                    @Override
                                    public void onRewardedAdFailedToShow(AdError adError) {
                                        // Ad failed to display.
                                        Toast.makeText(activityContext, "Ad failed to display.", Toast.LENGTH_SHORT).show();
                                    }
                                };
                                rewardedAd.show(activityContext, adCallback);
                            } else {
                                Toast.makeText(EpisodCollection.this, "The ad wasn't loaded yet.", Toast.LENGTH_SHORT).show();
                            }
                            

                        }//onitemclick
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }//oncomplet
        });


    }//getdata
}//mainclass