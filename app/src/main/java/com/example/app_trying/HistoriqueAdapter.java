package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smilerating.SmileRating;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {

    private List<user> mData;
    private HistoriqueAdapter.ItemClickListener mClickListener;
    String mComment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String myUid = mAuth.getInstance().getCurrentUser().getUid();
    String userID,tripID;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    // data is passed into the constructor
    HistoriqueAdapter(List<user> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public HistoriqueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup p0, int p1) {
        View mInflater =  LayoutInflater.from(p0.getContext()).inflate(R.layout.passager_item,p0,false);
        context = p0.getContext();
        return new HistoriqueAdapter.ViewHolder(mInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriqueAdapter.ViewHolder holder, int i) {

        final user userInfo = mData.get(i);

        userID = userInfo.id;



        // holder.ProfileImage.set(TripInfo.getmImageprofile());

        final StorageReference profileImageRef = storageReference
                .child(userInfo.getId()+".jpg");

        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (isValidContextForGlide(context)){
                    Glide.with(context).load(uri.toString()).into(holder.ProfileImage);

                }
            }
        });

        holder.NameUser.setText(userInfo.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();

                LayoutInflater inflater = LayoutInflater.from(context);
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                final SmileRating myRating = alertLayout.findViewById(R.id.smile_rating);

                final AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("How was your Experience with "+userInfo.name);

                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);

                myRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
                    @Override
                    public void onRatingSelected(int level, boolean reselected) {
                        alert.dismiss();
                        showSecondDialog(context,level);
                    }
                });

                alert.show();


            }
        });





    }

    private void showSecondDialog(Context context, final Integer level) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference().child("Evaluation").child(userID);
        final DatabaseReference pushedTripsRef = mReference.push();
        final String EvaluationId = pushedTripsRef.getKey();
        final String myLevel = level.toString();

        final AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        final EditText editText =  dialogView.findViewById(R.id.edt_comment);
        Button button1 =  dialogView.findViewById(R.id.buttonSubmit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment = editText.getText().toString();
                String date12 = df.format(Calendar.getInstance().getTime());
                Evaluation myEval = new Evaluation(EvaluationId,myUid,myLevel,mComment,date12);
                pushedTripsRef.setValue(myEval);
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        de.hdodenhof.circleimageview.CircleImageView ProfileImage;
        TextView NameUser;
        TextView Time;


        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ProfileImage = itemView.findViewById(R.id.profile_image1);
            NameUser = itemView.findViewById(R.id.nameChauff1);
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
    void setClickListener(HistoriqueAdapter.ItemClickListener itemClickListener) {
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
