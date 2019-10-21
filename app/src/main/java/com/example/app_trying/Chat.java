package com.example.app_trying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {


    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;

    CircleImageView profileImage;
    TextView userName;
    ImageButton btnSend;
    EditText messageSend;

    FirebaseUser fuser;
    DatabaseReference mReference;

    Intent intent;

    ChatAdapter chatAdapter;
    List<MessageModel> mList;

    RecyclerView mRecycler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecycler = findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManag = new LinearLayoutManager(getApplicationContext());
        linearLayoutManag.setStackFromEnd(true);
        mRecycler.setLayoutManager(linearLayoutManag);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();

        profileImage = findViewById(R.id.profileimageMsg);
        userName = findViewById(R.id.username);
        btnSend = findViewById(R.id.btn_send);
        messageSend = findViewById(R.id.text_send);

        intent = getIntent();
        final String userId = intent.getStringExtra("Recepteur");
        final StorageReference profileImageRef = storageReference
                .child(userId+".jpg");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("users").child(userId);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageSend.getText().toString();
                if (!msg.equals("")){
                    SendMessage(fuser.getUid(),userId,msg);
                }else {
                    Toast.makeText(Chat.this,"You can't send empty message",Toast.LENGTH_LONG).show();
                }

                messageSend.setText("");
            }
        });




        final Context context = getApplicationContext();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user User = dataSnapshot.getValue(user.class);

                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (isValidContextForGlide(context)){
                            Glide.with(getApplicationContext()).load(uri.toString()).into(profileImage);
                        }

                    }
                });

                userName.setText(User.getName());

                ReadMessages(fuser.getUid(),userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    private void SendMessage(String sender,String receiver,String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void ReadMessages(final String myId, final String userId){

        mList = new ArrayList<>();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    MessageModel message = snapshot.getValue(MessageModel.class);
                    if (message.getSender().equals(myId) && message.getReceiver().equals(userId) || message.getSender().equals(userId)
                     && message.getReceiver().equals(myId)){
                        mList.add(message);
                    }

                    chatAdapter = new ChatAdapter(mList,Chat.this);
                    mRecycler.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
