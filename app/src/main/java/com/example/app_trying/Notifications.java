package com.example.app_trying;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity implements NotifAdapter.ItemClickListener{

    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ArrayList<NotifModel> mNotifList;
    NotifAdapter mNotifAdapter;
    NotifModel Notif;
    RecyclerView mRecyclerView;
    String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        mAuth = FirebaseAuth.getInstance();

        Toolbar my2Toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(my2Toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Notif = new NotifModel();
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Attente");
        mNotifList = new ArrayList<>();
        mNotifAdapter = new NotifAdapter(mNotifList);
        user = mAuth.getCurrentUser().getUid();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotifList.clear();
               // myProgress2.setVisibility(View.VISIBLE);
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                  //  myProgress2.setVisibility(View.INVISIBLE);
                    Notif = ds.getValue(NotifModel.class);

                    if ((user.equals(Notif.getIdChauff()) && Notif.getType().equals("1"))
                    || (user.equals(Notif.getIdPass()) && Notif.getType().equals("2"))
                            || (user.equals(Notif.getIdPass()) && Notif.getType().equals("3"))
                            || (user.equals(Notif.getIdChauff()) && Notif.getType().equals("4"))){

                    mNotifList.add(Notif);}
                }

                mRecyclerView.setAdapter(mNotifAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
       // myProgress2.setVisibility(View.INVISIBLE);
        mNotifAdapter.setClickListener(this);


    }

    //------------------------- Click on Item of the RecyclerView --------------------------------

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked " + mNotifAdapter.getItem(position) + " on row number " + position,Toast.LENGTH_LONG).show();
    }

}
