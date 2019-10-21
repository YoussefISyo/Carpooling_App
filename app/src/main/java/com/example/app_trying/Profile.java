package com.example.app_trying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class Profile extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView profilePhoto;
    CollapsingToolbarLayout toolbar;
    TextView profileName,NbrEval;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef,mReference;
    final private int REQUEST_CODE = 1;
    Uri uriProfileImage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    de.hdodenhof.circleimageview.CircleImageView image_btn;
    public String profileImageUrl;
    Button deconnecter;
    Button ModifierProfile,historique;
    RatingBar myRating;
    float Rating = 0;
    int Number = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        mAuth = FirebaseAuth.getInstance();

        final String userID = mAuth.getCurrentUser().getUid();

        Toolbar my2Toolbar = findViewById(R.id.My2toolbar);
        setSupportActionBar(my2Toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = findViewById(R.id.toolbar_layout);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(Profile.this,EvaluationDetails.class);
                newActivity.putExtra("idProfile",userID);
                startActivity(newActivity);
            }
        });


        image_btn = findViewById(R.id.image_signup);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });


        loadUserInformation();


        deconnecter = findViewById(R.id.deconnecté);
        deconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent goToLogin = new Intent(Profile.this, MainActivity.class);
                startActivity(goToLogin);
            }
        });

        ModifierProfile = findViewById(R.id.modifierprofil);
        ModifierProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToModify = new Intent(Profile.this, ModifierProfile.class);
                startActivity(goToModify);
            }
        });

        historique = findViewById(R.id.historique);
        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToHistorique = new Intent(Profile.this, Historique.class);
                startActivity(goToHistorique);
            }
        });

        //------------------------------------------------------------------------------------------

        myRating = findViewById(R.id.ratingBar);
        NbrEval = findViewById(R.id.nbrEvaluation);
        mReference = mDatabase.getReference().child("Evaluation").child(userID);
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

    private void loadUserInformation() {
        final ProgressBar mProgress = findViewById(R.id.progressBar);
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();
        final String myId = user.getUid();
        final DatabaseReference userRef = mRef.child(myId);
        final StorageReference profileImageRef = storageReference
                .child(myId+".jpg");

        if (user != null) {
           // Toast.makeText(Profile.this, userRef.toString(), Toast.LENGTH_SHORT).show();
            profilePhoto = findViewById(R.id.image_signup);
            profileName = findViewById(R.id.userNameProfile);
            final Context context = getApplicationContext();

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mProgress.setVisibility(View.VISIBLE);

                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (isValidContextForGlide(context)){
                                Glide.with(Profile.this).load(uri.toString()).into(profilePhoto);

                                mProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data.getKey().equals("name")){
                            String userName = data.getValue().toString();
                           profileName.setText(userName);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if () {
            menu.findItem(R.id.report).setVisible(true);
            menu.findItem(R.id.block).setVisible(true);
        }else {
            menu.findItem(R.id.report).setVisible(false);
            menu.findItem(R.id.block).setVisible(false);
        }
        return true;
    } */



    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                image_btn.setImageBitmap(bitmap);
                uploadImageToFirebase();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void uploadImageToFirebase() {

        String UserId = mAuth.getCurrentUser().getUid();
        final StorageReference profileImageRef = storageReference
                .child(UserId+".jpg");

        if (uriProfileImage != null){
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profileImageUrl = profileImageRef.getDownloadUrl().toString();


                        }
                    });
        }
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
}