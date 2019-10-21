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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.app_trying.Chat;
import com.example.app_trying.MessageModel;
import com.example.app_trying.R;
import com.example.app_trying.user;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<user> mData;
    private ItemClickListener mClickListener;
    String theLastMessage;

    public Context mContext;

    UsersAdapter(ArrayList<user> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.message_item,p0,false);
        return new UsersAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapter.ViewHolder holder, int i) {

        final user User = mData.get(i);


        lastMessage(User.getId(),holder.LastMsg);

        holder.userName.setText(User.getName());
        //  holder.LastMsg.setText(MessageInfo.getMessage());
        // holder.TimeMsg.setText(MessageInfo.getReceiver());
        final StorageReference profileImageRef = FirebaseStorage.getInstance()
                .getReference().child(User.getId()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(mContext)) {
                    Glide.with(mContext).load(uri.toString()).into(holder.ProfileImage);
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent intent = new Intent(context, Chat.class);

                intent.putExtra("Recepteur",User.getId());
                // intent.putExtra("Recepteur",MessageInfo.getIdRecepteur());
                // intent.putExtra("NameRecept",MessageInfo.getNameRecept());
                //And so on for the rest of the data that you want to pass to
                //the second activity

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ProfileImage;
        TextView userName;
        TextView LastMsg;
        TextView TimeMsg;



        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

             ProfileImage = itemView.findViewById(R.id.profile_image_msg);
            userName = itemView.findViewById(R.id.userNameMsg);
            LastMsg = itemView.findViewById(R.id.lastMsg);



        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    user getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(UsersAdapter.ItemClickListener itemClickListener) {
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

    public void lastMessage(final String userId, final TextView lastMsg){

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Chats");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    MessageModel msg = snapshot.getValue(MessageModel.class);

                    if (msg.getSender().equals(fuser.getUid()) && msg.getReceiver().equals(userId) ||
                            msg.getSender().equals(userId) && msg.getReceiver().equals(fuser.getUid())){
                        theLastMessage = msg.getMessage();
                    }
                }

                lastMsg.setText(theLastMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
