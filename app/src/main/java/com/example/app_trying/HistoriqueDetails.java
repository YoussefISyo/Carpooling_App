package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smilerating.SmileRating;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoriqueDetails extends AppCompatActivity implements HistoriqueAdapter.ItemClickListener{

    Button demandeSupprim,modifier;
    RecyclerView mRecyclerView;
    ArrayList<user> mTripList = new ArrayList<>();
    HistoriqueAdapter mTripAdapter;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference,mReference3;
    TextView prix,nameChauff,rdvDepart,villeDepart,timeDepart,rdvTime,villearrivée,date1,comment,note;
    View view1,view2;
    ImageView imageChauff;
    LinearLayout conducteur;
    String nameUser,nameChauffeur1,mComment,idChauff;
    user user1;
    ArrayList<String> mPassengerId = new ArrayList<>();
    String idPassengeram;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    final String myUid = mAuth.getInstance().getCurrentUser().getUid();
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_details);

        mAuth = FirebaseAuth.getInstance();
        final String myUid = mAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();

        final String idChauffeur = getIntent().getStringExtra("idChauffeur");
        final String idTrip = getIntent().getStringExtra("idTrip");
        final String prixTrip = getIntent().getStringExtra("Prix");
        final String nameChauffeur = getIntent().getStringExtra("nameChauff");
        final String VilleDepart = getIntent().getStringExtra("Depart");
        final String Time = getIntent().getStringExtra("Time");
        final String VilleArrivée = getIntent().getStringExtra("Arrivée");
        final String dateTrip = getIntent().getStringExtra("Date");
        final String CommentTrip = getIntent().getStringExtra("Comment");
        final String SeatsTrip = getIntent().getStringExtra("Seats");

        nameChauffeur1 = nameChauffeur;
        idChauff = idChauffeur;

        user1 = new user();
        mRecyclerView = findViewById(R.id.recyclerview2);

        String realdate = dateTrip.replace("-"," ");
        date1 = findViewById(R.id.dateToolbar);
        date1.setText("Le "+realdate);


        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReference = mDatabase.getReference().child("users").child(myUid);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals("name")) {
                        nameUser = data.getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mReference = mDatabase.getReference().child("Trips").child(idTrip);
        demandeSupprim = findViewById(R.id.demandSupprim);
        modifier = findViewById(R.id.modifier);
        if (myUid.equals(idChauffeur) == false){

        }else{
            demandeSupprim.setBackgroundResource(R.drawable.background_login);
            demandeSupprim.setTextColor(Color.BLACK);
            demandeSupprim.setText("Supprimer");
            modifier.setVisibility(View.VISIBLE);
        }



        demandeSupprim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (demandeSupprim.getText().equals("Supprimer")){
                    mReference.setValue(null);
                    Intent goToHome = new Intent(HistoriqueDetails.this,Historique.class);
                    startActivity(goToHome);
                }

            }
        });


        prix = findViewById(R.id.prix);
        prix.setText(prixTrip+" DZ");

        imageChauff = findViewById(R.id.image_offre);

        nameChauff = findViewById(R.id.nameChauff);
        nameChauff.setText(nameChauffeur);

        rdvDepart = findViewById(R.id.rdvDepart);
        rdvDepart.setText(VilleDepart);

        villeDepart = findViewById(R.id.villedepart);
        villeDepart.setText(VilleDepart);

        timeDepart = findViewById(R.id.timedepart);
        timeDepart.setText(Time);

        villearrivée = findViewById(R.id.villeArrivée);
        villearrivée.setText(VilleArrivée);

        note = findViewById(R.id.note);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        comment = findViewById(R.id.commentaire);

         final Context context = getApplicationContext();

        final StorageReference profileImageRef = storageReference
                .child(idChauffeur+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(HistoriqueDetails.this).load(uri.toString()).into(imageChauff);

                }
            }
        });

        conducteur = findViewById(R.id.conductrice);
      //  if (myUid != idChauffeur)
        conducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Context context1 = v.getContext();
                    LayoutInflater inflater = LayoutInflater.from(context1);
                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                    final SmileRating myRating = alertLayout.findViewById(R.id.smile_rating);

                    final AlertDialog alert = new AlertDialog.Builder(context1).create();
                    alert.setTitle("How was your Experience with " + nameChauffeur);

                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(true);

                    myRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
                        @Override
                        public void onRatingSelected(int level, boolean reselected) {
                           // Toast.makeText(context1, level + " Selected", Toast.LENGTH_LONG).show();
                            alert.dismiss();
                            showSecondDialog(context1, level);
                        }
                    });

                    alert.show();



            }


        });
        if ("".equals(CommentTrip)){
            comment.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }else{
            comment.setVisibility(View.VISIBLE);
            comment.setText(CommentTrip);
        }

        mReference3 = mDatabase.getReference().child("Passenger").child(idTrip);
        mTripAdapter = new HistoriqueAdapter(mTripList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    idPassengeram = data.getValue().toString();
                    Toast.makeText(HistoriqueDetails.this,idPassengeram,Toast.LENGTH_LONG).show();
                    getUsers(idPassengeram);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mTripAdapter.setClickListener(this);


    }

    private void showSecondDialog(Context context, final Integer level) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference().child("Evaluation").child(idChauff);
        final DatabaseReference pushedTripsRef = mReference.push();
        final String EvaluationId = pushedTripsRef.getKey();
        final String myLevel = level.toString();
        final String date12 = df.format(Calendar.getInstance().getTime());

        final AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        final EditText editText =  dialogView.findViewById(R.id.edt_comment);
        Button button1 =  dialogView.findViewById(R.id.buttonSubmit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment = editText.getText().toString();

                Evaluation myEval = new Evaluation(EvaluationId,myUid,myLevel,mComment,date12);
                pushedTripsRef.setValue(myEval);
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void getUsers(String mPassengerId) {
        user1 = new user();
        mRecyclerView = findViewById(R.id.recyclerview2);

        mTripAdapter = new HistoriqueAdapter(mTripList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mReference4 = mDatabase.getReference().child("users");

        mReference4.child(mPassengerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    user1 = dataSnapshot.getValue(user.class);
                    mTripList.add(user1);



                mRecyclerView.setAdapter(mTripAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked " + mTripAdapter.getItem(position) + " on row number " + position,Toast.LENGTH_LONG).show();
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

