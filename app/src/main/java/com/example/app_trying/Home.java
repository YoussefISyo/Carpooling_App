package com.example.app_trying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity implements TripAdapter.ItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mRecyclerView;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference, mReference1;
    ArrayList<Trip> mTripList = new ArrayList<>();
    TripAdapter mTripAdapter;
    de.hdodenhof.circleimageview.CircleImageView myImae;
    Trip trip;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FloatingActionButton myFab;
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter2 = new SimpleDateFormat("kk:mm");
    String formattedDate,oldDate,formattedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        mAuth = FirebaseAuth.getInstance();

        final String UserId = mAuth.getCurrentUser().getUid();
        final StorageReference profileImageRef = storageReference.child(UserId + ".jpg");

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Haya M3aya");


        DrawerLayout mDrawer = findViewById(R.id.mDrawer);

        ActionBarDrawerToggle actionToggle = new ActionBarDrawerToggle(this, mDrawer, myToolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawer.addDrawerListener(actionToggle);
        actionToggle.syncState();

        NavigationView mNavigation = findViewById(R.id.mNavig);
        mNavigation.setNavigationItemSelectedListener(this);

        Menu myMenu = mNavigation.getMenu();
        MenuItem allUsers = myMenu.findItem(R.id.Users);
        if (UserId.equals("2i4RuyPTMrYAKHrdF4WTEpINc2v2")){
            allUsers.setVisible(true);
        }


        final ProgressBar myProgress2 = findViewById(R.id.progressBarHome);
        trip = new Trip();
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Trips");

        mTripAdapter = new TripAdapter(mTripList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReference1 = mDatabase.getReference().child("TripArchivé");



        //---------------------- Get Data from Firebase ---------------------------

        myProgress2.setVisibility(View.VISIBLE);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    trip = ds.getValue(Trip.class);

                    VerifyDate(trip.getIdTrip());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        myProgress2.setVisibility(View.INVISIBLE);


        //-------------------------------------------------------------------------


        myProgress2.setVisibility(View.VISIBLE);

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTripList.clear();
                myProgress2.setVisibility(View.VISIBLE);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myProgress2.setVisibility(View.INVISIBLE);
                    trip = ds.getValue(Trip.class);


                    mTripList.add(trip);
                }
                mRecyclerView.setAdapter(mTripAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        myProgress2.setVisibility(View.INVISIBLE);
        mTripAdapter.setClickListener(this);

        //--------------------------------------------------------------------------
        loadNavigationInfo();

        myFab = findViewById(R.id.myFab);
        myFab.show();
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddOfer = new Intent(Home.this, addOffer.class);
                startActivity(gotoAddOfer);
            }
        });

    }

    private void VerifyDate(final String idTripTrip) {

        mDatabase = FirebaseDatabase.getInstance();
        mReference1 = mDatabase.getReference("Trips");
        final DatabaseReference mReference2 = mDatabase.getReference().child("TripsArchivé");
        mReference1.child(idTripTrip).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       Trip myTrip = dataSnapshot.getValue(Trip.class);

                        String month = myTrip.getmDate().substring(3, myTrip.getmDate().length() - 5);
                        String day =  myTrip.getmDate().substring(0, 2);
                        String year = myTrip.getmDate().substring(myTrip.getmDate().length() - 4);

                        String i = null;
                        switch (month) {
                            case "Janvier":
                                i = "01";
                                break;
                            case "Février":
                                i = "02";
                                break;
                            case "Mars":
                                i = "03";
                                break;
                            case "Avril":
                                i = "04";
                                break;
                            case "Mai":
                                i = "05";
                                break;
                            case "Juin":
                                i = "06";
                                break;
                            case "Juillet":
                                i = "07";
                                break;
                            case "Aout":
                                i = "08";
                                break;
                            case "Septembre":
                                i = "09";
                                break;
                            case "Octobre":
                                i = "10";
                                break;
                            case "Novembre":
                                i = "11";
                                break;
                            case "Décembre":
                                i = "12";
                                break;
                        }
                        oldDate = day + "-" + i + "-" + year;
                        Date currentDate = Calendar.getInstance().getTime();

                        formattedDate = df.format(currentDate);
                       // Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT).show();

                        if (!oldDate.equals(formattedDate)) {

                        } else if (oldDate.compareTo(formattedDate) < 0){
                            mReference1.orderByKey().equalTo(idTripTrip).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Trip trip1 = ds.getValue(Trip.class);
                                        mReference2.child(idTripTrip).setValue(trip1);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mReference1.child(idTripTrip).setValue(null);
                        }else{
                            VerifyTime(idTripTrip);
                        }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void VerifyTime(final String idTrip1){

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Trips");
        final DatabaseReference mReference2 = mDatabase.getReference().child("TripsArchivé");
        mReference.child(idTrip1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Trip myTrip = dataSnapshot.getValue(Trip.class);
                        Date currentDate = Calendar.getInstance().getTime();
                        formattedTime = formatter2.format(currentDate);

                        if (myTrip.getmTime().compareTo(formattedTime) >= 0){

                        }else{

                            mReference.orderByKey().equalTo(idTrip1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Trip trip1 = ds.getValue(Trip.class);
                                        mReference2.child(idTrip1).setValue(trip1);

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mReference.child(idTrip1).setValue(null);

                        }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void loadNavigationInfo() {

        String UserId = mAuth.getInstance().getCurrentUser().getUid();
        final StorageReference profileImageRef = storageReference
                .child(UserId+".jpg");
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");
        final DatabaseReference userRef = mReference.child(UserId);

        NavigationView navigationView = findViewById(R.id.mNavig);
        View navHeader = navigationView.getHeaderView(0);
        final ImageView yImage = navHeader.findViewById(R.id.profile_image_navig);
        final TextView userName = navHeader.findViewById(R.id.UserNameHeaderNav);
        final TextView email = navHeader.findViewById(R.id.emailHeaderNav);


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user muUser = dataSnapshot.getValue(user.class);
                userName.setText(muUser.getName());
                email.setText(muUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(yImage);
            }
        });

    }

    // ------------------ Create Menu (Search and SignOut) --------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notifications){
            Intent Navig = new Intent(Home.this, Notifications.class);
            startActivity(Navig);

        }
        if (id == R.id.search){
            Intent Navig = new Intent(Home.this, Recherche.class);
            startActivity(Navig);

        }
        return true;
    }

    //------------------------- Click on Item of the RecyclerView --------------------------------

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked " + mTripAdapter.getItem(position) + " on row number " + position,Toast.LENGTH_LONG).show();
    }

    //------------------------ Click on Navigation item ------------------------------------------

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch(id){
            case R.id.My_Profile:
                Intent Navig = new Intent(Home.this, Profile.class);
                startActivity(Navig);
                break;

            case R.id.Chat:
                Intent Navig1 = new Intent(Home.this, Messages.class);
                startActivity(Navig1);
                break;

               /* case R.id.Offers:
                Intent Navig2 = new Intent(Home.this, Home.class);
                startActivity(Navig2);
                break;*/

            case R.id.demands:
                Intent Navig3 = new Intent(Home.this, Demands.class);
                startActivity(Navig3);
                break;

            case R.id.MyOffers:
                Intent Navig4 = new Intent(Home.this, MyOffers.class);
                startActivity(Navig4);
                break;

            case R.id.MyDemands:
                Intent Navig5 = new Intent(Home.this, MyDemands.class);
                startActivity(Navig5);
                break;

            case R.id.Users:
                Intent Navig6 = new Intent(Home.this, Signaler.class);
                startActivity(Navig6);
                break;
        }


        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        DrawerLayout mDrawer = findViewById(R.id.mDrawer);
        mDrawer.closeDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = findViewById(R.id.mDrawer);
        if (mDrawer.isDrawerOpen(Gravity.START))
            closeDrawer();
        else
            super.onBackPressed();
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
    protected void onStart() {
        super.onStart();

        final ProgressBar myProgress2 = findViewById(R.id.progressBarHome);
        trip = new Trip();
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Trips");

        myProgress2.setVisibility(View.VISIBLE);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myProgress2.setVisibility(View.VISIBLE);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myProgress2.setVisibility(View.INVISIBLE);
                    trip = ds.getValue(Trip.class);
                    VerifyDate(trip.getIdTrip());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        myProgress2.setVisibility(View.INVISIBLE);
    }
}
