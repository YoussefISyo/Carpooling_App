package com.example.app_trying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

public class Signaler extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SignalAdapter.ItemClickListener {

    FloatingActionButton fab;
    RecyclerView mRecyclerView;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference, mReference1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    ArrayList<user> mUsersList = new ArrayList<>();
    SignalAdapter mSignalAdapter;
    user trip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        fab = findViewById(R.id.myFab);
        fab.hide();

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
        allUsers.setVisible(true);

        final ProgressBar myProgress2 = findViewById(R.id.progressBarHome);
        trip = new user();
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users");

        mSignalAdapter = new SignalAdapter(mUsersList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myProgress2.setVisibility(View.VISIBLE);

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersList.clear();
                myProgress2.setVisibility(View.VISIBLE);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myProgress2.setVisibility(View.INVISIBLE);
                    trip = ds.getValue(user.class);
                    if (!trip.getId().equals("2i4RuyPTMrYAKHrdF4WTEpINc2v2")){
                    mUsersList.add(trip);}
                }
                mRecyclerView.setAdapter(mSignalAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        myProgress2.setVisibility(View.INVISIBLE);
        mSignalAdapter.setClickListener(this);


        loadNavigationInfo();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch(id){
            case R.id.My_Profile:
                Intent Navig = new Intent(Signaler.this, Profile.class);
                startActivity(Navig);
                break;

            case R.id.Chat:
                Intent Navig1 = new Intent(Signaler.this, Messages.class);
                startActivity(Navig1);
                break;

                case R.id.Offers:
                Intent Navig2 = new Intent(Signaler.this, Home.class);
                startActivity(Navig2);
                break;

            case R.id.demands:
                Intent Navig3 = new Intent(Signaler.this, Demands.class);
                startActivity(Navig3);
                break;

            case R.id.MyOffers:
                Intent Navig4 = new Intent(Signaler.this, MyOffers.class);
                startActivity(Navig4);
                break;

            case R.id.MyDemands:
                Intent Navig5 = new Intent(Signaler.this, MyDemands.class);
                startActivity(Navig5);
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
    public void onItemClick(View view, int position) {
    }
}
