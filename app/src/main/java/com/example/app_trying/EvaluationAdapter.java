package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.hsalf.smilerating.SmileRating;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.ViewHolder> {

    private List<Evaluation> mData;
    private EvaluationAdapter.ItemClickListener mClickListener;
    String mComment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String myUid = mAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mReference;
    String userID,tripID;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    // data is passed into the constructor
    EvaluationAdapter(List<Evaluation> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public EvaluationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.evaluation_item,p0,false);
        context = p0.getContext();
        return new EvaluationAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final EvaluationAdapter.ViewHolder holder, int i) {

        final Evaluation EvalInfo = mData.get(i);

        mReference = FirebaseDatabase.getInstance().getReference("users").child(EvalInfo.getIdEvaluer());

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user Evaluer = dataSnapshot.getValue(user.class);
                holder.NameUser.setText(Evaluer.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final StorageReference profileImageRef = storageReference
                .child(EvalInfo.getIdEvaluer()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(context).load(uri.toString()).into(holder.ProfileImage);

                }
            }
        });
        holder.date.setText(EvalInfo.getTime());
        holder.comment.setText(EvalInfo.getComment());
        holder.myRating.setRating(Integer.parseInt(EvalInfo.getLevel()));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        de.hdodenhof.circleimageview.CircleImageView ProfileImage;
        TextView NameUser;
        TextView comment;
        TextView date;
        RatingBar myRating;


        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ProfileImage = itemView.findViewById(R.id.imageEval);
            NameUser = itemView.findViewById(R.id.nameEval);
            comment = itemView.findViewById(R.id.commentEval);
            date = itemView.findViewById(R.id.dateEval);
            myRating = itemView.findViewById(R.id.ratingBarmy);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    Evaluation getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(EvaluationAdapter.ItemClickListener itemClickListener) {
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
