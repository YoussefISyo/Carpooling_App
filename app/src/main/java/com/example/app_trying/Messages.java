package com.example.app_trying;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messages extends AppCompatActivity implements UsersAdapter.ItemClickListener{

    RecyclerView mRecyclerView;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ArrayList<user> mUsersList;
    UsersAdapter mMessageAdapter;
    user User;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ArrayList<String> UsersList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_layout);

        mAuth = FirebaseAuth.getInstance();

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Haya M3aya");

        final ArrayList<String> myMssgd = new ArrayList();



        final ProgressBar myProgress2 = findViewById(R.id.progressBarMsg);
        User = new user();
        mRecyclerView = findViewById(R.id.recyclerviewMsg);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Chats");
        mUsersList = new ArrayList<>();
        mMessageAdapter = new UsersAdapter(mUsersList,Messages.this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //---------------------- Get Data from Firebase ---------------------------

        final String UserId = mAuth.getInstance().getCurrentUser().getUid();

        Query queryRef = mReference.orderByChild("time");

        myProgress2.setVisibility(View.VISIBLE);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersList.clear();
                mUsersList.clear();
                myProgress2.setVisibility(View.VISIBLE);
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    myProgress2.setVisibility(View.INVISIBLE);
                    MessageModel chat = ds.getValue(MessageModel.class);
                    if (chat.getSender().equals(UserId)) {
                        UsersList.add(chat.getReceiver()); }
                    if(chat.getReceiver().equals(UserId)){
                            UsersList.add(chat.getSender()); }
                }
                if (!UserId.equals("2i4RuyPTMrYAKHrdF4WTEpINc2v2") && UsersList.contains("2i4RuyPTMrYAKHrdF4WTEpINc2v2")){
                    UsersList.remove("2i4RuyPTMrYAKHrdF4WTEpINc2v2");
                }

                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked " + mMessageAdapter.getItem(position) + " on row number " + position,Toast.LENGTH_LONG).show();
    }

    public void readChats(){
        mUsersList = new ArrayList<>();
        User = new user();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("users");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User = snapshot.getValue(user.class);

                    for (String id : UsersList){
                        if (User.getId().equals(id)){
                            if (mUsersList.size() != 0){
                                for (user User1 : mUsersList){
                                    if (!User.getId().equals(User1.getId())){
                                        mUsersList.add(User);
                                    }
                                }

                            }else{
                                mUsersList.add(User);
                            }
                        }
                    }

                }

                mMessageAdapter = new UsersAdapter(mUsersList,getApplicationContext());
                mRecyclerView.setAdapter(mMessageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMessageAdapter.setClickListener(this);
    }
}
