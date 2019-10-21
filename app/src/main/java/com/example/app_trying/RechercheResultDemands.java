package com.example.app_trying;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class RechercheResultDemands extends AppCompatActivity implements DemandAdapter.ItemClickListener{

    RecyclerView mRecycler;
    DemandAdapter mTripAdapter2;
    TextView Vide;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_layout);

        Toolbar myTool = findViewById(R.id.myToolbar);
        setSupportActionBar(myTool);
        getSupportActionBar().setTitle("Resultat de Recherche");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Vide = findViewById(R.id.listevide);

        ArrayList<DemandModel> myList = (ArrayList<DemandModel>) getIntent().getSerializableExtra("myList");

        if (myList.size() == 0){
            Vide.setVisibility(View.VISIBLE);
        }

        mRecycler = findViewById(R.id.recyclerview);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mTripAdapter2 = new DemandAdapter(myList);

        mRecycler.setAdapter(mTripAdapter2);

        mTripAdapter2.setClickListener(this);

    }


    @Override
    public void onItemClick(View view, int position) {

    }

}
