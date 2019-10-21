package com.example.app_trying;

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

public class Demands extends AppCompatActivity implements DemandAdapter.ItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    RecyclerView mRecyclerView;
    FirebaseAuth mAuth = null;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ArrayList<DemandModel> mDemandList;
    DemandAdapter mDemandAdapter;
    de.hdodenhof.circleimageview.CircleImageView myImae;
    DemandModel demand;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demands_layout);

        mAuth = FirebaseAuth.getInstance();

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Haya M3aya");

        DrawerLayout mDrawer = findViewById(R.id.mDrawer);

        ActionBarDrawerToggle actionToggle = new ActionBarDrawerToggle(this,mDrawer,myToolbar,R.string.drawer_open,
                R.string.drawer_close);

        mDrawer.addDrawerListener(actionToggle);
        actionToggle.syncState();

        NavigationView mNavigation = findViewById(R.id.mNavig);
        mNavigation.setNavigationItemSelectedListener(this);

        final String UserId = mAuth.getCurrentUser().getUid();
        Menu myMenu = mNavigation.getMenu();
        MenuItem allUsers = myMenu.findItem(R.id.Users);
        if (UserId.equals("2i4RuyPTMrYAKHrdF4WTEpINc2v2")){
            allUsers.setVisible(true);
        }

        final ProgressBar myProgress2 = findViewById(R.id.progressBarHome);
        demand = new DemandModel();
        mRecyclerView = findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Demands");
        mDemandList = new ArrayList<>();
        mDemandAdapter = new DemandAdapter(mDemandList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //---------------------- Get Data from Firebase ---------------------------

        myProgress2.setVisibility(View.VISIBLE);
        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myProgress2.setVisibility(View.VISIBLE);
                mDemandList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    myProgress2.setVisibility(View.INVISIBLE);
                    demand = ds.getValue(DemandModel.class);

                    mDemandList.add(demand);
                }
                myProgress2.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mDemandAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        myProgress2.setVisibility(View.INVISIBLE);
        mDemandAdapter.setClickListener(this);

        //-------------------------------------------------------------------------

        loadNavigationInfo();

        myFab = findViewById(R.id.myFab);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddOfer = new Intent(Demands.this, addDemands.class);
                startActivity(gotoAddOfer);
            }
        });


    }

    private void loadNavigationInfo() {

        String UserId = mAuth.getCurrentUser().getUid();
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
            Intent Navig = new Intent(Demands.this, Notifications.class);
            startActivity(Navig);
        }
        if (id == R.id.search){
            Intent Navig = new Intent(Demands.this, RechercheDemands.class);
            startActivity(Navig);

        }
        return true;
    }

    //------------------------- Click on Item of the RecyclerView --------------------------------

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You clicked " + mDemandAdapter.getItem(position) + " on row number " + position,Toast.LENGTH_LONG).show();
    }

    //------------------------ Click on Navigation item ------------------------------------------

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch(id){
            case R.id.My_Profile:
                Intent Navig = new Intent(Demands.this, Profile.class);
                startActivity(Navig);
                break;

            case R.id.Chat:
                Intent Navig1 = new Intent(Demands.this, Messages.class);
                startActivity(Navig1);
                break;

            case R.id.Offers:
                Intent Navig2 = new Intent(Demands.this, Home.class);
                startActivity(Navig2);
                break;

            /*case R.id.demands:
                Intent Navig3 = new Intent(Demands.this, Demands.class);
                startActivity(Navig3);
                break;*/

            case R.id.MyOffers:
                Intent Navig4 = new Intent(Demands.this, MyOffers.class);
                startActivity(Navig4);
                break;

            case R.id.MyDemands:
                Intent Navig5 = new Intent(Demands.this, MyDemands.class);
                startActivity(Navig5);
                break;

            case R.id.Users:
                Intent Navig6 = new Intent(Demands.this, Signaler.class);
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
}
