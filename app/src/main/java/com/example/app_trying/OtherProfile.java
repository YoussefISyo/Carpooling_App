package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.app_trying.Profile.isValidContextForGlide;

public class OtherProfile extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    DatabaseReference mRef;

    GregorianCalendar cal = new GregorianCalendar();
    de.hdodenhof.circleimageview.CircleImageView profilePhoto;
    TextView profileName,ageUser,comment,cigarette,discussion,ambiance,numtel,voiture,cartype,apropos,otherEval,NbrEval;
    View view1,view2,view3,view4;
    Button MsgBtn;
    String idChauffeur1;
    String nameChauff;
    String cause;
    RatingBar myRating;
    float Rating = 0;
    int Number = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile);

        FirebaseUser user = mAuth.getCurrentUser();
        final String myId = user.getUid();

        final String idChauffeur = getIntent().getStringExtra("idChauff");
        idChauffeur1 = idChauffeur;

        MsgBtn = findViewById(R.id.button2);


        if (myId.equals(idChauffeur1)){
            MsgBtn.setVisibility(View.GONE);
        }


        Toolbar my2Toolbar = findViewById(R.id.My2toolbar);
        setSupportActionBar(my2Toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadUserInformation();

        otherEval = findViewById(R.id.commentaireeteval);
        otherEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(OtherProfile.this,EvaluationDetails.class);
                newActivity.putExtra("idProfile",idChauffeur1);
                startActivity(newActivity);
            }
        });

        myRating = findViewById(R.id.ratingBar);
        NbrEval = findViewById(R.id.nbrEvaluation);
        mReference = mDatabase.getReference().child("Evaluation").child(idChauffeur1);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Evaluation newEval = data.getValue(Evaluation.class);

                    String userName = newEval.getLevel();
                    int level = Integer.parseInt(userName);
                    Rating = Rating + level;
                    Number++;


                }
                Rating = Rating / Number;
                myRating.setRating(Rating);
                if (Number<2){
                    NbrEval.setText(Number+" Utilisateur");
                }else{
                    NbrEval.setText(Number+" Utilisateurs");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //-------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final FirebaseUser user = mAuth.getCurrentUser();
        final Context context = getApplicationContext();


        if (id == R.id.report) {

            if (!user.getUid().equals(idChauffeur1)) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pourquoi vous voulez signaler cet utilisateur ! ");

                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_comment, null);

                builder.setView(dialogView);
                TextView txtView = dialogView.findViewById(R.id.textView);
                txtView.setVisibility(View.GONE);
                final EditText editText = dialogView.findViewById(R.id.edt_comment);
                Button button1 = dialogView.findViewById(R.id.buttonSubmit);
                final AlertDialog dialog = builder.create();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cause = editText.getText().toString();

                        MessageModel signalUser = new MessageModel(user.getUid(), "2i4RuyPTMrYAKHrdF4WTEpINc2v2", "Le Signalé est " + idChauffeur1 + "\n La Cause est " + cause);
                        reference.child("Chats").push().setValue(signalUser);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        }
        return true;
    }

    //-------------------------------------------------------------------------

    private void loadUserInformation() {
        final ProgressBar mProgress = findViewById(R.id.progressBar);
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();
        final String myId = user.getUid();
        final DatabaseReference userRef = mRef.child(idChauffeur1);
        final StorageReference profileImageRef = storageReference
                .child(idChauffeur1+".jpg");

        if (user != null) {
            // Toast.makeText(Profile.this, userRef.toString(), Toast.LENGTH_SHORT).show();
            profilePhoto = findViewById(R.id.image_signup);
            comment = findViewById(R.id.comment);
            cigarette = findViewById(R.id.cigarette);
            ambiance = findViewById(R.id.ambiance);
            discussion = findViewById(R.id.discussion);
            apropos = findViewById(R.id.aproposde);
            voiture = findViewById(R.id.voiture);
            cartype = findViewById(R.id.cartype);
            view3  =findViewById(R.id.view03);
            view4 = findViewById(R.id.view04);
            numtel = findViewById(R.id.numtel);
            view1 = findViewById(R.id.view01);
            view2 = findViewById(R.id.view02);
            profileName = findViewById(R.id.userNameProfile);
            ageUser = findViewById(R.id.ageUser);
            final Context context = getApplicationContext();

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mProgress.setVisibility(View.VISIBLE);

                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (isValidContextForGlide(context)){
                                Glide.with(OtherProfile.this).load(uri.toString()).into(profilePhoto);

                                mProgress.setVisibility(View.INVISIBLE);
                            }

                        }
                    });

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data.getKey().equals("name")){
                            nameChauff = data.getValue().toString();
                            profileName.setText(nameChauff);
                        }
                        if(data.getKey().equals("dateNaissance")){
                            String age = data.getValue().toString();
                            if (age.equals("")){
                                ageUser.setVisibility(View.GONE);
                            }else{
                                age = age.substring(age.length()-4);
                                int year = Integer.parseInt(age);
                                year = cal.get(Calendar.YEAR) - year;
                                ageUser.setText(String.valueOf(year)+" Ans");
                            }
                        }

                        if(data.getKey().equals("propos")){
                            String commentai = data.getValue().toString();
                            if (!commentai.equals("")){
                                comment.setVisibility(View.VISIBLE);
                                comment.setText(commentai);
                                apropos.setVisibility(View.VISIBLE);
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                            }
                        }

                        if(data.getKey().equals("ambiance")){
                            String ambianceS = data.getValue().toString();
                            if (!ambianceS.equals("")){
                                ambiance.setVisibility(View.VISIBLE);
                                ambiance.setText(ambianceS);
                                apropos.setVisibility(View.VISIBLE);
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                            }
                        }

                        if(data.getKey().equals("cigarette")){
                            String cigaretteS = data.getValue().toString();
                            if (!cigaretteS.equals("")){
                                cigarette.setVisibility(View.VISIBLE);
                                cigarette.setText(cigaretteS);
                                apropos.setVisibility(View.VISIBLE);
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                            }
                        }

                        if(data.getKey().equals("discussion")){
                            String discussionS = data.getValue().toString();
                            if (!discussionS.equals("")){
                                discussion.setVisibility(View.VISIBLE);
                                discussion.setText(discussionS);
                                apropos.setVisibility(View.VISIBLE);
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                            }
                        }

                        if(data.getKey().equals("phone")){
                            String phoneS = data.getValue().toString();
                            if (!phoneS.equals("")){
                                numtel.setVisibility(View.VISIBLE);

                            }
                        }

                        if(data.getKey().equals("voiture")){
                            String voitureS = data.getValue().toString();
                            if (!voitureS.equals("")){
                                cartype.setVisibility(View.VISIBLE);
                                voiture.setVisibility(View.VISIBLE);
                                view3.setVisibility(View.VISIBLE);
                                view4.setVisibility(View.VISIBLE);
                                cartype.setText(voitureS);

                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        MsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent = new Intent(OtherProfile.this, Chat.class);
                newintent.putExtra("Recepteur",idChauffeur1);
                //And so on for the rest of the data that you want to pass to
                //the second activity

                startActivity(newintent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (!user.getUid().equals(idChauffeur1)) {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            return true;
        }else{
            return true;
        }
    }
}
