package com.example.app_trying;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

    private List<NotifModel> mData;
    private NotifAdapter.ItemClickListener mClickListener;

    ArrayList<String> myList= new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference,mReference2,mReference3,mReference4,mReference5;
    public String PassagerName,InitLocat,FinalLocat;

    NotifAdapter(List<NotifModel> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public NotifAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.notif_item,p0,false);
        return new NotifAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.ViewHolder holder, int i) {

        final NotifModel NotifInfo = mData.get(i);

        VerifyExist(NotifInfo.getIdTrip(),NotifInfo.getIdAttente());
        String tupe = NotifInfo.getType();
        mReference = mDatabase.getReference().child("Trips").child(NotifInfo.getIdTrip());
        mReference = mDatabase.getReference().child("Demands").child(NotifInfo.getIdTrip());
        mReference2 = mDatabase.getReference().child("Attente").child(NotifInfo.getIdAttente());
        mReference3 = mDatabase.getReference().child("Attente");
        mReference5 = mDatabase.getReference().child("Trips");
        mReference4 = mDatabase.getReference().child("Passenger").child(NotifInfo.getIdTrip()).push();
        if (tupe.equals("1")){
        holder.myText.setText(NotifInfo.getNamePass()+" veut voyager avec vous de "+NotifInfo.getInitLocat()+" à "+NotifInfo.getFinalLocat());
        holder.accepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference pushedTripsRef = mReference3.push();
                final String attenteId = pushedTripsRef.getKey();

                mReference4.setValue(NotifInfo.getIdPass());

                NotifModel newNotif = new NotifModel(attenteId,NotifInfo.getIdTrip(),NotifInfo.getIdChauff(),NotifInfo.getIdPass()
                ,"2",NotifInfo.getNameChauff(),NotifInfo.getNamePass(),NotifInfo.getInitLocat(),NotifInfo.getFinalLocat());

                mReference2.setValue(null);
                pushedTripsRef.setValue(newNotif);



            }
        });
        holder.refuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference2.setValue(null);
            }
        });}

        //------------------------------------------------------------------------------------------

        if (tupe.equals("2")){
            holder.myText.setText(NotifInfo.getNameChauff()+" à accepté votre demande de voyager avec lui de "+NotifInfo.getInitLocat()+" à "+NotifInfo.getFinalLocat());
            holder.accepter.setVisibility(View.GONE);
            holder.refuser.setVisibility(View.GONE);
        }

        //------------------------------------------------------------------------------------------

        if (tupe.equals("3")){
            holder.myText.setText(NotifInfo.getNameChauff()+" veut être votre covoitureur de "+NotifInfo.getInitLocat()+" à "+NotifInfo.getFinalLocat());
            holder.accepter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference pushedTripsRef = mReference5.push();

                    final String idnewTrip = pushedTripsRef.getKey();
                    final DatabaseReference pushedTripsRef1 = mReference3.push();
                    final String attenteId = pushedTripsRef1.getKey();
                    mReference4 = mDatabase.getReference().child("Passenger").child(idnewTrip).push();

                    mReference4.setValue(NotifInfo.getIdPass());

                    Trip newTrip = new Trip(idnewTrip,NotifInfo.getIdChauff(),NotifInfo.getNameChauff(),NotifInfo.getInitLocat()
                    ,NotifInfo.getFinalLocat(),NotifInfo.getDate(),NotifInfo.getTime(),NotifInfo.getPrix(),NotifInfo.getPlaces(),NotifInfo.getComment());

                    pushedTripsRef.setValue(newTrip);

                    NotifModel newNotif = new NotifModel(attenteId,NotifInfo.getIdTrip(),NotifInfo.getIdChauff(),NotifInfo.getIdPass()
                            ,"4",NotifInfo.getNameChauff(),NotifInfo.getNamePass(),NotifInfo.getInitLocat(),NotifInfo.getFinalLocat());

                    mReference2.setValue(null);
                    mReference.setValue(null);

                    pushedTripsRef1.setValue(newNotif);

                }
            });
            holder.refuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReference2.setValue(null);
                }
            });
            
        }

        //------------------------------------------------------------------------------------------

        if (tupe.equals("4")){
            holder.myText.setText(NotifInfo.getNamePass()+" à accepté votre demande de covoiturage de "+NotifInfo.getInitLocat()+" à "+NotifInfo.getFinalLocat());
            holder.accepter.setVisibility(View.GONE);
            holder.refuser.setVisibility(View.GONE);

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent goToDetail = new Intent(context,OfferDetails.class);
                //goToDetail.putExtra("idChauffeur",TripInfo.getIdChauffeur());

                context.startActivity(goToDetail);


            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView myText;
        Button accepter;
        Button refuser;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            myText = itemView.findViewById(R.id.textnotif);
            accepter = itemView.findViewById(R.id.accepter);
            refuser = itemView.findViewById(R.id.refuser);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    NotifModel getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(NotifAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void VerifyExist(final String idTrip, final String idNotif){
        final ArrayList<String> Lists = new ArrayList<>();
        final DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Trips");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   Trip trip = snapshot.getValue(Trip.class);
                   Lists.add(trip.getIdTrip());
               }
               if (!Lists.contains(idTrip)){
                   DatabaseReference mRefer = FirebaseDatabase.getInstance().getReference("Attente").child(idNotif);
                   mRefer.setValue(null);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
