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
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SignalAdapter extends RecyclerView.Adapter<SignalAdapter.ViewHolder> {

    private List<user> mData;
    private SignalAdapter.ItemClickListener mClickListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;


    // data is passed into the constructor
    SignalAdapter(List<user> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public SignalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.user_item,p0,false);
        context = p0.getContext();
        return new SignalAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final SignalAdapter.ViewHolder holder, int i) {

        final user userinfo = mData.get(i);

        // holder.ProfileImage.set(TripInfo.getmImageprofile());
        final StorageReference profileImageRef = storageReference
                .child(userinfo.getId()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(context).load(uri.toString()).into(holder.ProfileImage);

                }
            }
        });


        holder.NameUser.setText(userinfo.getName());
        holder.idUser.setText(userinfo.getId());
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messages = new Intent(context,Chat.class);
                messages.putExtra("Recepteur",userinfo.getId());
                context.startActivity(messages);
            }
        });



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("users").child(userinfo.getId());
                mReference.setValue(null);
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
        TextView idUser;
        ImageButton message,delete;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ProfileImage = itemView.findViewById(R.id.profile_image_msg);
            NameUser = itemView.findViewById(R.id.userNameMsg);
            idUser = itemView.findViewById(R.id.idUser);
            message = itemView.findViewById(R.id.mssgUser);
            delete = itemView.findViewById(R.id.deleteUser);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    user getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(SignalAdapter.ItemClickListener itemClickListener) {
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

