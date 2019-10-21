package com.example.app_trying;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageModel> mData;
    private ItemClickListener mClickListener;
    String userName;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    public String myId2 = FirebaseAuth.getInstance().getUid();
    public Context mContext;
    MessageAdapter(List<MessageModel> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.message_item,p0,false);
        return new ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final MessageModel MessageInfo = mData.get(i);

        final String userId;

        if (MessageInfo.getReceiver().equals(fuser.getUid())){
            userId  = MessageInfo.getSender();
        }else{
            userId = MessageInfo.getReceiver();
        }

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user UserOth = dataSnapshot.getValue(user.class);
                userName = UserOth.name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.userName.setText(userName);
      //  holder.LastMsg.setText(MessageInfo.getMessage());
       // holder.TimeMsg.setText(MessageInfo.getReceiver());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

               Intent intent = new Intent(context, Chat.class);

                intent.putExtra("Recepteur",userId);
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

            // ProfileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameMsg);
            LastMsg = itemView.findViewById(R.id.lastMsg);


        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    MessageModel getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
