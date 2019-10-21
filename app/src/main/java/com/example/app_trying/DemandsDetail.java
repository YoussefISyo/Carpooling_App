package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DemandsDetail extends AppCompatActivity {

    Button demandeSupprim,modifier;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference,mReference3;
    TextView prix,nameChauff,rdvDepart,villeDepart,timeDepart,rdvTime,villearrivée,date1,comment,note;
    View view1,view2;
    ImageView imageChauff;
    String nameUser,mEdit1,mComment;
    LinearLayout conducteur;
    ImageButton chatBubble,phoneBubble;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offerdetails);

        mAuth = FirebaseAuth.getInstance();
        final String myUid = mAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        final String idChauffeur = getIntent().getStringExtra("idDemandeur");
        final String idTrip = getIntent().getStringExtra("idTrip");
        final String prixTrip = getIntent().getStringExtra("Prix");
        final String nameChauffeur = getIntent().getStringExtra("nameChauff");
        final String VilleDepart = getIntent().getStringExtra("Depart");
        final String Time = getIntent().getStringExtra("Time");
        final String VilleArrivée = getIntent().getStringExtra("Arrivée");
        final String dateTrip = getIntent().getStringExtra("Date");
        final String CommentTrip = getIntent().getStringExtra("Comment");

        final CharSequence[] mSeatsNew = {"1","2","3","4"};


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

        conducteur = findViewById(R.id.conduct);
        conducteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CondProf = new Intent(DemandsDetail.this,OtherProfile.class);
                CondProf.putExtra("idChauff",idChauffeur);
                startActivity(CondProf);
            }
        });

        chatBubble = findViewById(R.id.chat_bubble);
        chatBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idChauffeur.equals(myUid)){
                    Toast.makeText(DemandsDetail.this,"Tu peux pas contacter ton profile",Toast.LENGTH_LONG);
                }else {
                    Intent newIntent = new Intent(DemandsDetail.this, Chat.class);
                    newIntent.putExtra("Recepteur", idChauffeur);
                    startActivity(newIntent);
                }
            }
        });
        final DatabaseReference mRefer = FirebaseDatabase.getInstance().getReference("users").child(idChauffeur);
        phoneBubble = findViewById(R.id.phone_bubble);
        phoneBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user UserChauff = dataSnapshot.getValue(user.class);
                        if (UserChauff.getPhone() != null){
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+UserChauff.getPhone()));
                            startActivity(intent);
                        }else{
                            Toast.makeText(DemandsDetail.this,UserChauff.getName()+" n'a pas un numéro de téléphone!",Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



        mReference = mDatabase.getReference().child("Demands").child(idTrip);
        demandeSupprim = findViewById(R.id.demandSupprim);
        modifier = findViewById(R.id.modifier);
        if (myUid.equals(idChauffeur) == false){
            demandeSupprim.setBackgroundResource(R.drawable.background_confirmbtn);
            demandeSupprim.setTextColor(Color.WHITE);
            demandeSupprim.setText("Covoiturer");
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
                    Intent goToHome = new Intent(DemandsDetail.this,Demands.class);
                    startActivity(goToHome);
                }

                if (demandeSupprim.getText().equals("Covoiturer")) {
                    showDialog(mSeatsNew,"Nombre de place :");
                    mReference3 = mDatabase.getReference().child("Attente");
                    final DatabaseReference pushedTripsRef = mReference3.push();
                    final String attenteId = pushedTripsRef.getKey();

                    pushedTripsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            NotifModel newNotif = new NotifModel(attenteId,idTrip,myUid,idChauffeur,"3",nameUser,nameChauffeur,VilleDepart
                                    ,VilleArrivée,dateTrip,Time,mEdit1,prixTrip,mComment);
                            pushedTripsRef.setValue(newNotif);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        prix = findViewById(R.id.prix);
        prix.setText(prixTrip+" DZ");

        imageChauff = findViewById(R.id.image_offre);

        final Context context = getApplicationContext();

        final StorageReference profileImageRef = storageReference
                .child(idChauffeur+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(DemandsDetail.this).load(uri.toString()).into(imageChauff);

                }
            }
        });

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
        if (CommentTrip.equals("")){
            comment.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }else{
            comment.setVisibility(View.VISIBLE);
            comment.setText(CommentTrip);
        }

        rdvTime = findViewById(R.id.rdvTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date = dateFormat.parse(Time);
            Date date2 = dateFormat.parse("00:10");
            long diff = date.getTime() - date2.getTime();
            long timeInSeconds = diff / 1000;
            long hours, minutes;
            hours = timeInSeconds / 3600;
            timeInSeconds = timeInSeconds - (hours * 3600);
            minutes = timeInSeconds / 60;
            String diffTime = (hours<10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
            rdvTime.setText(diffTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotToModification = new Intent(DemandsDetail.this,DemandsModification.class);
                gotToModification.putExtra("idTripModif",idTrip);
                gotToModification.putExtra("chauffeurTripModif",idChauffeur);
                gotToModification.putExtra("namechauffeurTripModif",nameChauffeur);

                startActivity(gotToModification);
            }
        });

    }

    public void showDialog(final CharSequence[] mList, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(DemandsDetail.this);
        builder.setTitle(title);
        // final CharSequence[] mSexe = {"un Homme","une Femme"};
        builder.setItems(mList,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 mEdit1 = mList[which].toString();
                 showSecondDialogue();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showSecondDialogue() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        final EditText editText =  dialogView.findViewById(R.id.edt_comment);
        Button button1 =  dialogView.findViewById(R.id.buttonSubmit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment = editText.getText().toString();
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
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
