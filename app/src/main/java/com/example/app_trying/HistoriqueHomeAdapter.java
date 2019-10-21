package com.example.app_trying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HistoriqueHomeAdapter extends RecyclerView.Adapter<HistoriqueHomeAdapter.ViewHolder> {

    private List<Trip> mData;
    private HistoriqueHomeAdapter.ItemClickListener mClickListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;


    // data is passed into the constructor
    HistoriqueHomeAdapter(List<Trip> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public HistoriqueHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.trip_item,p0,false);
        context = p0.getContext();
        return new HistoriqueHomeAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriqueHomeAdapter.ViewHolder holder, int i) {

        final Trip TripInfo = mData.get(i);


        final StorageReference profileImageRef = storageReference
                .child(TripInfo.getIdChauffeur()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(context).load(uri.toString()).into(holder.ProfileImage);

                }
            }
        });




        holder.NameUser.setText(TripInfo.getmName());
        holder.Date.setText(TripInfo.getmDate());
        holder.Initlocat.setText("From : " +TripInfo.getmInitLocat());
        holder.finalLocat.setText("To : " +TripInfo.getmFinalLocat());
        holder.Time.setText(TripInfo.getmTime());
        holder.Price.setText(TripInfo.getmPrice()+" DZ");
        holder.Seats.setText(TripInfo.getmSeatsAvail()+" Seats");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent goToDetail = new Intent(context,HistoriqueDetails.class);
                goToDetail.putExtra("idChauffeur",TripInfo.getIdChauffeur());
                goToDetail.putExtra("idTrip",TripInfo.getIdTrip());
                goToDetail.putExtra("nameChauff",TripInfo.getmName());
                goToDetail.putExtra("Prix",TripInfo.getmPrice());
                goToDetail.putExtra("Depart",TripInfo.getmInitLocat());
                goToDetail.putExtra("Time",TripInfo.getmTime());
                goToDetail.putExtra("Arrivée",TripInfo.getmFinalLocat());
                goToDetail.putExtra("Date",TripInfo.getmDate());
                goToDetail.putExtra("Comment",TripInfo.getmComment());
                goToDetail.putExtra("Seats",TripInfo.getmSeatsAvail());
                context.startActivity(goToDetail);


            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        de.hdodenhof.circleimageview.CircleImageView ProfileImage;
        TextView NameUser;
        TextView Initlocat;
        TextView finalLocat;
        TextView Date ;
        TextView Time;
        TextView Price;
        TextView Seats;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ProfileImage = itemView.findViewById(R.id.profile_image);
            NameUser = itemView.findViewById(R.id.name);
            Initlocat = itemView.findViewById(R.id.from);
            finalLocat = itemView.findViewById(R.id.to);
            Date = itemView.findViewById(R.id.date);
            Time = itemView.findViewById(R.id.time);
            Price = itemView.findViewById(R.id.price);
            Seats = itemView.findViewById(R.id.seats);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    Trip getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(HistoriqueHomeAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
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
