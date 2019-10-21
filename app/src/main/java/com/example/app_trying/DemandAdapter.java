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

public class DemandAdapter extends RecyclerView.Adapter<DemandAdapter.ViewHolder> {

    private List<DemandModel> mData;
    private DemandAdapter.ItemClickListener mClickListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;


    // data is passed into the constructor
    DemandAdapter(List<DemandModel> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public DemandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.demand_item,p0,false);
        context = p0.getContext();
        return new DemandAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final DemandAdapter.ViewHolder holder, int i) {

        final DemandModel DemandInfo = mData.get(i);

        final StorageReference profileImageRef = storageReference
                .child(DemandInfo.getIdDemandeur()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(context).load(uri.toString()).into(holder.ProfileImage);

                }
            }
        });

        // holder.ProfileImage.set(TripInfo.getmImageprofile());
        holder.NameUser.setText(DemandInfo.getjName());
        holder.Date.setText(DemandInfo.getjDate());
        holder.Initlocat.setText("From : " +DemandInfo.getjInitLocat());
        holder.finalLocat.setText("To : " +DemandInfo.getjFinalLocat());
        holder.Time.setText(DemandInfo.getjTime());
        holder.Price.setText(DemandInfo.getjPrice()+" DZ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent goToDetail = new Intent(context,DemandsDetail.class);
                goToDetail.putExtra("idDemandeur",DemandInfo.getIdDemandeur());
                goToDetail.putExtra("idTrip",DemandInfo.getIdTrip());
                goToDetail.putExtra("nameChauff",DemandInfo.getjName());
                goToDetail.putExtra("Prix",DemandInfo.getjPrice());
                goToDetail.putExtra("Depart",DemandInfo.getjInitLocat());
                goToDetail.putExtra("Time",DemandInfo.getjTime());
                goToDetail.putExtra("Arrivée",DemandInfo.getjFinalLocat());
                goToDetail.putExtra("Date",DemandInfo.getjDate());
                goToDetail.putExtra("Comment",DemandInfo.getjComment());


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

    DemandModel getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(DemandAdapter.ItemClickListener itemClickListener) {
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
