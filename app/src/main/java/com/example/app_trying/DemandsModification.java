package com.example.app_trying;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DemandsModification extends AppCompatActivity {

    EditText depart,destin,modifDate,modifTime,modifPrix,modifComment;
    TextView modifPlaces;
    ImageButton modifM, ModifP;
    Button enregistrer;
    FirebaseAuth mAuth = null;
    DatabaseReference mReference3;
    int nbrPlace;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference,mReference2;
    List<String> list=new ArrayList();
    List<String> listComm=new ArrayList();
    DatePickerDialog.OnDateSetListener mDateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_modification);

        mAuth = FirebaseAuth.getInstance();

        final String idTripModif = getIntent().getStringExtra("idTripModif");
        final String idChauffeurModif = getIntent().getStringExtra("chauffeurTripModif");
        final String nameChauffeurModif = getIntent().getStringExtra("namechauffeurTripModif");

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Modifier Votre Demande");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String UserUid = mAuth.getCurrentUser().getUid();
        mReference3 = FirebaseDatabase.getInstance().getReference("Demands").child(idTripModif);

        depart = findViewById(R.id.modifDepart);
        destin = findViewById(R.id.modifDestination);
        modifDate = findViewById(R.id.modifDate);
        modifTime = findViewById(R.id.modifTime);
        modifPrix = findViewById(R.id.modifPrix);
        modifPlaces = findViewById(R.id.modifplacestext);
        modifComment = findViewById(R.id.modifComment);

        mReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DemandModel newDemands = dataSnapshot.getValue(DemandModel.class);

                depart.setText(newDemands.getjInitLocat());
                destin.setText(newDemands.getjFinalLocat());
                modifDate.setText(newDemands.getjDate());
                modifTime.setText(newDemands.getjTime());
                modifPrix.setText(newDemands.getjPrice());
                modifComment.setText(newDemands.getjComment());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWilayas(depart);
            }
        });

        destin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWilayas(destin);
            }
        });

        modifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickADateMethod();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String monthlet = "";
                switch (month) {
                    case 1:
                        monthlet = "Janvier";
                        break;
                    case 2:
                        monthlet = "Février";
                        break;
                    case 3:
                        monthlet = "Mars";
                        break;
                    case 4:
                        monthlet = "Avril";
                        break;
                    case 5:
                        monthlet = "Mai";
                        break;
                    case 6:
                        monthlet = "Juin";
                        break;
                    case 7:
                        monthlet = "Juillet";
                        break;
                    case 8:
                        monthlet = "Aout";
                        break;
                    case 9:
                        monthlet = "Septembre";
                        break;
                    case 10:
                        monthlet = "Octobre";
                        break;
                    case 11:
                        monthlet = "Novembre";
                        break;
                    case 12:
                        monthlet = "Décembre";
                        break;
                }
                String date = dayOfMonth + "-" + monthlet + "-" + year;
                modifDate.setText(date);
            }
        };

        modifTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setATimeMethod();
            }
        });

        enregistrer = findViewById(R.id.enregistrer);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!depart.getText().toString().equals("") && !destin.getText().toString().equals("") && !modifDate.getText().toString().equals("")
                        && !modifTime.getText().toString().equals("") && !modifPrix.getText().toString().equals("")){

                    DemandModel finalOne = new DemandModel(idTripModif,idChauffeurModif,nameChauffeurModif,depart.getText().toString(),destin.getText().toString(),
                            modifDate.getText().toString(),modifTime.getText().toString(),modifPrix.getText().toString(),modifComment.getText().toString());

                    mReference3.setValue(finalOne);

                    Intent goToDemands = new Intent(DemandsModification.this,Demands.class);
                    startActivity(goToDemands);
                }
            }
        });
    }

    private void getWilayas(final EditText DepartBtn) {
        mReference = mDatabase.getReference("Wilayas");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Wilayas myWil = data.getValue(Wilayas.class);
                    String userName1 = myWil.getNom();
                    list.add(userName1);
                }

                showDialog(DepartBtn);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialog(final EditText DepartBtn) {

        // setup the alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Wilaya : ");

        // add a list
        final CharSequence[] mWilayas = list.toArray(new String[list.size()]);
        builder.setItems(mWilayas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nomWilaya = mWilayas[which].toString();
                communesDialog(which,nomWilaya,DepartBtn);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void communesDialog(final int which, final String nomWilaya, final EditText DepartBtn) {
        mReference2 = mDatabase.getReference("Communes");

        mReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComm.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Communes myComm = data.getValue(Communes.class);
                    int wilayaId = Integer.parseInt(myComm.getWilaya_id());
                    if (wilayaId == which+1){
                        String CommuneName = myComm.getNom();
                        listComm.add(CommuneName);
                    } }
                showDialogComm(listComm,nomWilaya,DepartBtn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogComm(List<String> listComm, final String nomWilaya, final EditText DepartBtn) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose a Commune : ");
        // add a list
        final CharSequence[] mCommunes = listComm.toArray(new String[listComm.size()]);
        builder1.setItems(mCommunes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DepartBtn.setText(nomWilaya+" { " +mCommunes[which].toString()+" }");
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder1.create();
        dialog.show();

    }

    private void pickADateMethod() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(DemandsModification.this,
                R.style.CustomDatePickerDialog,
                mDateListener,
                year,month,day);

        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        dialog.show();

    }

    private void setATimeMethod() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(DemandsModification.this,R.style.Theme_MyTheme_TimePicker,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hours = Integer.toString(hourOfDay);
                String minutes = Integer.toString(minute);
                if (hours.length() == 1){
                    if (minutes.length() == 1){
                        modifTime.setText( "0"+hours + ":" + "0"+minutes);
                    }else{
                        modifTime.setText( "0"+hours + ":" + minutes);
                    }
                }else{
                    if (minutes.length() == 1){
                        modifTime.setText( hours + ":" + "0"+minutes);
                    }else{
                        modifTime.setText( hours + ":" + minutes);
                    }
                }
            }
        },hour, minute, true);


        // mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
