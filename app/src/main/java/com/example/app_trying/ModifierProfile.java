package com.example.app_trying;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ModifierProfile extends AppCompatActivity {

    FirebaseAuth mAuth = null;
    DatabaseReference mDatabase;
    DatePickerDialog.OnDateSetListener mDateListener;
    EditText nom;
    EditText email;
    EditText phone;
    EditText voiture;
    EditText sexe;
    EditText nationalité;
    EditText ambiance;
    EditText cigarette;
    EditText discussion;
    EditText dateNaissance;
    Button enregistrer;
    EditText propos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifierprofile_layout);

        mAuth = FirebaseAuth.getInstance();


        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Modifier Votre profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String UserUid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(UserUid);

        sexe = findViewById(R.id.sexe);
        nationalité = findViewById(R.id.nationalité);
        ambiance = findViewById(R.id.ambiance);
        cigarette = findViewById(R.id.cigarette);
        discussion = findViewById(R.id.discussion);
        dateNaissance = findViewById(R.id.naissance);
        nom = findViewById(R.id.etUsername);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.telephone);
        propos = findViewById(R.id.apropos);
        voiture = findViewById(R.id.voiture);



        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user oldone = dataSnapshot.getValue(user.class);
                nom.setText(oldone.getName());
                email.setText(oldone.getEmail());
                phone.setText(oldone.getPhone());
                dateNaissance.setText(oldone.getDateNaissance());
                sexe.setText(oldone.getSexe());
                nationalité.setText(oldone.getNationalité());
                voiture.setText(oldone.getVoiture());
                ambiance.setText(oldone.getAmbiance());
                cigarette.setText(oldone.getCigarette());
                discussion.setText(oldone.getDiscussion());
                propos.setText(oldone.getPropos());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        final CharSequence[] mSexe = {"un Homme","une Femme"};
        final CharSequence[] mNation = {"Afrique du Sud","Afghanistan","Albanie","Algérie","Allemagne","Andorre","Angola","Antigua-et-Barbuda","Arabie Saoudite","Argentine","Arménie",
                "Australie","Autriche","Azerbaïdjan","Bahamas","Bahreïn","Bangladesh","Barbade","Belgique","Belize","Bénin","Bhoutan","Biélorussie","Birmanie","Bolivie","Bosnie-Herzégovine","Botswana","Brésil","Brunei","Bulgarie",
                "Burkina Faso","Burundi","Cambodge","Cameroun","Canada","Cap-Vert","Chili","Chine","Chypre","Colombie","Comores","Corée du Nord","Corée du Sud","Costa Rica","Côte d’Ivoire","Croatie",
        "Cuba","Danemark","Djibouti","Dominique","Égypte","Émirats arabes unis","Équateur","Érythrée","Espagne","Eswatini","Estonie","États-Unis","Éthiopie","Fidji","Finlande","France","Gabon","Gambie",
        "Géorgie","Ghana","Grèce","Grenade","Guatemala","Guinée","Guinée équatoriale","Guinée-Bissau","Guyana","Haïti","Honduras","Hongrie","Îles Cook","Îles Marshall",
        "Inde","Indonésie","Irak","Iran","Irlande","Islande","Italie","Jamaïque","Japon","Jordanie","Kazakhstan","Kenya","Kirghizistan","Kiribati","Koweït","Laos","Lesotho","Lettonie",
        "Liban","Liberia","Libye","Liechtenstein","Lituanie","Luxembourg","Macédoine","Madagascar","Malaisie","Malawi","Maldives","Mali","Malte","Maroc","Maurice","Mauritanie","Mexique","Micronésie",
        "Moldavie","Monaco","Mongolie","Monténégro","Mozambique","Namibie","Nauru","Népal","Nicaragua","Niger","Nigeria","Niue","Norvège","Nouvelle-Zélande","Oman","Ouganda",
        "Ouzbékistan","Pakistan","Palaos","Palestine","Panama","Papouasie-Nouvelle-Guinée","Paraguay","Pays-Bas","Pérou","Philippines","Pologne","Portugal",
        "Qatar","République centrafricaine","République démocratique du Congo","République Dominicaine","République du Congo","République tchèque","Roumanie","Royaume-Uni","Russie","Rwanda","Saint-Kitts-et-Nevis",
        "Saint-Vincent-et-les-Grenadines","Sainte-Lucie","Saint-Marin","Salomon","Salvador","Samoa","São Tomé-et-Principe","Sénégal","Serbie","Seychelles","Sierra Leone",
        "Singapour","Slovaquie","Slovénie","Somalie","Soudan","Soudan du Sud","Sri Lanka","Suède","Suisse","Suriname","Syrie","Tadjikistan","Tanzanie","Tchad","Thaïlande","Timor oriental","Togo","Tonga",
        "Trinité-et-Tobago","Tunisie","Turkménistan","Turquie","Tuvalu","Ukraine","Uruguay","Vanuatu","Vatican","Venezuela","Viêt Nam","Yémen","Zambie","Zimbabwe"};
        final CharSequence[] mAmiance = {"J'aime bien écouter de la musique","J'aime bien écouter les infos",
                             "J'aime le calme en voiture"};
        final CharSequence[] mCigarette = {"La Cigarette ne me dérange pas","La Cigarette me dérange"};
        final CharSequence[] mDiscussion = {"J'aime bien discuter","je discute de temps à autre",
                             "je suis plutot discret en voiture"};



        sexe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mSexe,"Je suis :",sexe);
            }
        });
        nationalité.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mNation,"Ma nationalité :",nationalité);
            }
        });
        ambiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mAmiance,"Ambiance dans la voiture :",ambiance);
            }
        });
        cigarette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mCigarette,"Cigarette :",cigarette);
            }
        });
        discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mDiscussion,"Les discussions en voiture :",discussion);
            }
        });
        dateNaissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ModifierProfile.this,
                        R.style.CustomDatePickerDialog,
                        mDateListener,
                        year,month,day);

                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                dialog.show();
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
                String date = dayOfMonth + " - " + monthlet + " - " + year;
                dateNaissance.setText(date);
            }
        };

        enregistrer = findViewById(R.id.enregistrer);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nom.getText().toString().equals("") && !email.getText().toString().equals("") && !phone.getText().toString().equals("")
                 && !dateNaissance.getText().toString().equals("")){
                    user newOne = new user(UserUid,nom.getText().toString(),email.getText().toString(),phone.getText().toString(),nationalité.getText().toString(),
                            dateNaissance.getText().toString(),sexe.getText().toString(),voiture.getText().toString(),ambiance.getText().toString(),
                            cigarette.getText().toString(),discussion.getText().toString(),propos.getText().toString());

                    mDatabase.setValue(newOne);

                    Intent goToProfile = new Intent(ModifierProfile.this,Profile.class);
                    startActivity(goToProfile);
                }
                else{
                    Toast.makeText(ModifierProfile.this, "Completer les champs nécessaires SVP ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showDialog(final CharSequence[] mList, String title, final EditText mEdit){
        AlertDialog.Builder builder = new AlertDialog.Builder(ModifierProfile.this);
        builder.setTitle(title);
       // final CharSequence[] mSexe = {"un Homme","une Femme"};
        builder.setItems(mList,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEdit.setText(mList[which].toString());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
