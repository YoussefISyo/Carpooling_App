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

public class RechercheDemands extends AppCompatActivity {

    FirebaseAuth mAuth = null;
    EditText DepartBtn,DestinationBtn,pickaDate,setaTime,type;
    Button rechercherBtn;
    Button annulerBtn;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference,mReference5;
    DatabaseReference mReference2;
    List<String> list=new ArrayList();
    List<String> listComm=new ArrayList();
    DatePickerDialog.OnDateSetListener mDateListener;
    String dayReal;
    ValueEventListener mlistener;
    Boolean depart,destin,time,date;
    Boolean one = true,two = true,three = true,four = true;
    ArrayList<DemandModel> mDemandsList = new ArrayList<>();
    DemandModel trip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recherche_layout);

        mDatabase = FirebaseDatabase.getInstance();

        final CharSequence[] mType = {"Offre","Demande"};

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Recherche");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DepartBtn = findViewById(R.id.depart);
        DepartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWilayas(DepartBtn);
            }
        });

        DestinationBtn = findViewById(R.id.destination);
        DestinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWilayas(DestinationBtn);
            }
        });

        type = findViewById(R.id.type);
        type.setText("Demande");

        pickaDate = findViewById(R.id.pickaDate);
        pickaDate.setOnClickListener(new View.OnClickListener() {
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
                if (dayOfMonth<10){
                    dayReal = "0"+ dayOfMonth;
                }
                else{
                    dayReal = String.valueOf(dayOfMonth);
                }

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
                String date = dayReal + "-" + monthlet + "-" + year;
                pickaDate.setText(date);
            }
        };


        setaTime = findViewById(R.id.setTime);
        setaTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setATimeMethod();
            }
        });


        annulerBtn  = findViewById(R.id.annulerBtn2);
        annulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTrips = new Intent(RechercheDemands.this,Demands.class);
                startActivity(goToTrips);
            }
        });
        trip = new DemandModel();

        rechercherBtn = findViewById(R.id.rechercheBtn);
        mReference5 = mDatabase.getReference("Demands");

        rechercherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemandsList.clear();
                if (DepartBtn.getText().toString().equals("")){depart = true; }else{depart = false; }

                if (DestinationBtn.getText().toString().equals("")){ destin = true; }else{ destin = false; }

                if (setaTime.getText().toString().equals("")){ time = true; }else{ time = false; }

                if (pickaDate.getText().toString().equals("")){ date = true; }else{ date = false; }


                mReference5.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            trip = ds.getValue(DemandModel.class);
                            if (!depart) {
                                if (!trip.getjInitLocat().equals(DepartBtn.getText().toString())) {
                                    one = false;
                                }else{
                                    one = true;
                                }
                            }
                            if (!destin) {
                                if (!trip.getjFinalLocat().equals(DestinationBtn.getText().toString())) {
                                    two = false;
                                }else{
                                    two = true;
                                }
                            }
                            if (!time) {
                                if (!trip.getjTime().equals(setaTime.getText().toString())) {
                                    three = false;
                                }else{
                                    three = true;
                                }
                            }
                            if (!date) {
                                if (!trip.getjDate().equals(pickaDate.getText().toString())) {
                                    four = false;
                                }else{
                                    four = true;
                                }
                            }

                            if (one && two && three && four) {
                                mDemandsList.add(trip);

                            }
                        }
                        Intent goToAhaaah = new Intent(RechercheDemands.this, RechercheResultDemands.class);
                        goToAhaaah.putExtra("myList", mDemandsList);

                        startActivity(goToAhaaah);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


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
                DepartBtn.setText(nomWilaya+" - " +mCommunes[which].toString());
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder1.create();
        dialog.show();

    }

    private void setATimeMethod() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(RechercheDemands.this,R.style.Theme_MyTheme_TimePicker,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hours = Integer.toString(hourOfDay);
                String minutes = Integer.toString(minute);
                if (hours.length() == 1){
                    if (minutes.length() == 1){
                        setaTime.setText( "0"+hours + ":" + "0"+minutes);
                    }else{
                        setaTime.setText( "0"+hours + ":" + minutes);
                    }
                }else{
                    if (minutes.length() == 1){
                        setaTime.setText( hours + ":" + "0"+minutes);
                    }else{
                        setaTime.setText( hours + ":" + minutes);
                    }
                }
            }
        },hour, minute, true);


        // mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickADateMethod() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(RechercheDemands.this,
                R.style.CustomDatePickerDialog,
                mDateListener,
                year,month,day);

        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        dialog.show();

    }

}
