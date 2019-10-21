package com.example.app_trying;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class addOffer extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    FirebaseAuth mAuth = null;
    EditText DepartBtn;
    EditText DestinationBtn;
    EditText pickaDate;
    EditText setaTime;
    EditText prixtxt;
    EditText comment;
    Button confirmbtn;
    Button annulerBtn;
    ImageButton moinsbtn;
    ImageButton plusbtn;
    TextView placestxt;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    DatabaseReference mReference2;
    List<String> list=new ArrayList();
    List<String> listComm=new ArrayList();
    DatePickerDialog.OnDateSetListener mDateListener;
    String realDate,dayReal;
    ValueEventListener mlistener;

    int nbrPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addoffer_layout);

        mDatabase = FirebaseDatabase.getInstance();

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ajouter un voyage");
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

        moinsbtn = findViewById(R.id.moinsBtn);
        plusbtn = findViewById(R.id.plusBtn);
        placestxt = findViewById(R.id.placestext);
        comment = findViewById(R.id.comment);

        nbrPlace = 0;
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbrPlace == 4) {
                    nbrPlace = 0;
                } else {
                    nbrPlace++;
                }
                placestxt.setText("" + nbrPlace);
            }
        });
        moinsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nbrPlace == 0) nbrPlace = 4;
                else nbrPlace--;
                placestxt.setText("" + nbrPlace);
            }
        });

        prixtxt = findViewById(R.id.prixtxt);
        prixtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prixtxt.setFocusable(true);
            }
        });

        confirmbtn = findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DepartBtn.getText().equals("") && !DestinationBtn.getText().equals("") && !pickaDate.getText().equals("")
                && !setaTime.getText().equals("") && !prixtxt.getText().equals("") && nbrPlace!=0){

                    saveTrip();
                    Intent goToHome = new Intent(addOffer.this,Home.class);
                    startActivity(goToHome);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Completer toutes les champs SVP",Toast.LENGTH_LONG).show();
                }
            }
        });

        annulerBtn = findViewById(R.id.annulerBtn);
        annulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTrips = new Intent(addOffer.this,Home.class);
                startActivity(goToTrips);
            }
        });


    }

    private void saveTrip() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Trips");
        final String myUid = mAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference pushedTripsRef = mReference.push();
        final String postId = pushedTripsRef.getKey();

        final String[] finalName = new String[1];

        //DepartBtn = findViewById(R.id.depart);
        DepartBtn.getText();
        //DestinationBtn = findViewById(R.id.destination);
        DestinationBtn.getText();
        pickaDate.getText();
        setaTime.getText();
        prixtxt.getText();
        placestxt.getText();
        comment.getText();

        String Useridtrip = mAuth.getInstance().getCurrentUser().getUid();
       DatabaseReference ReferenceName = mDatabase.getReference().child("users").child(Useridtrip).child("name");
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference mPhoto = mStorage.getReference().child(Useridtrip+".jpg");
        //Task<Uri> url = mPhoto.getDownloadUrl();
       ReferenceName.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               finalName[0] = dataSnapshot.getValue(String.class);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        mlistener = pushedTripsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Trip mTrip = new Trip(postId,myUid, finalName[0],DepartBtn.getText().toString(),DestinationBtn.getText().toString(),pickaDate.getText().toString(),
                        setaTime.getText().toString(),prixtxt.getText().toString(),placestxt.getText().toString(),comment.getText().toString());

                pushedTripsRef.setValue(mTrip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setATimeMethod() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(addOffer.this,R.style.Theme_MyTheme_TimePicker,new TimePickerDialog.OnTimeSetListener() {
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

        DatePickerDialog dialog = new DatePickerDialog(addOffer.this,
                R.style.CustomDatePickerDialog,
                mDateListener,
                year,month,day);

        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        dialog.show();

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

    @Override
    protected void onStop() {
        super.onStop();

        mReference = mDatabase.getReference("Trips");

        if (mlistener != null)
            mReference.removeEventListener(mlistener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mReference = mDatabase.getReference("Trips");

        if (mlistener != null)
        mReference.removeEventListener(mlistener);
    }
}
