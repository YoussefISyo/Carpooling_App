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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_RIGHT = 1;
    public static final int MSG_LEFT = 0;

    FirebaseAuth mAuth = null;
    private List<MessageModel> mData;
    private Context mContext;
    private ChatAdapter.ItemClickListener mClickListener;
    private String imageUrl;

    FirebaseUser fuser;

    ChatAdapter(List<MessageModel> data,Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        if (p1 == MSG_RIGHT) {
            View mInflater = LayoutInflater.from(p0.getContext()).inflate(R.layout.chat_item_right, p0, false);
            return new ChatAdapter.ViewHolder(mInflater);
        }else{
            View mInflater = LayoutInflater.from(p0.getContext()).inflate(R.layout.chat_item_left, p0, false);
            return new ChatAdapter.ViewHolder(mInflater);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, int i) {

        final MessageModel MessageInfo = mData.get(i);



        String myUid = mAuth.getInstance().getCurrentUser().getUid();
        final StorageReference profileImageRef = FirebaseStorage.getInstance()
                .getReference().child(MessageInfo.getSender()+".jpg");

        holder.show_message.setText(MessageInfo.getMessage());

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(mContext)) {
                    Glide.with(mContext).load(uri.toString()).into(holder.ProfileImage);
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ProfileImage;
        TextView show_message;




        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            // ProfileImage = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.show_message);
            ProfileImage = itemView.findViewById(R.id.profile_image_chat);



        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mData.get(position).getSender().equals(fuser.getUid())){
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }

    MessageModel getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ChatAdapter.ItemClickListener itemClickListener) {
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
