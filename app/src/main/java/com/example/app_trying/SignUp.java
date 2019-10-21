package com.example.app_trying;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;



public class SignUp extends AppCompatActivity {

    public String profileImageUrl;
    Uri uriProfileImage;
    private String imgPath;
    final private int REQUEST_CODE = 1;
    de.hdodenhof.circleimageview.CircleImageView image_btn;
    FirebaseAuth mAuth = null;
    DatabaseReference mDatabase;
    String profileImage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // --------------- Go to Login Layout ----------------------

        TextView btnToLogin = findViewById(R.id.intenttologin);

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogin = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(goToLogin);
            }
        });


        // ------------- Signup and Go to Login -----------------

        Button Signup = findViewById(R.id.btnget_started);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();

            }

        });

        //-------------------------------------------------------

        //----------------- Pick a Profile Image ----------------
      /*  image_btn = findViewById(R.id.image_signup);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        }); */
        //------------------------------------------------------



    }

    private void saveUserInformation() {
        EditText nameSignup = findViewById(R.id.nameSignup);
        String userName = nameSignup.getText().toString();

        EditText emailSignup = findViewById(R.id.mailSignup);
        String email = emailSignup.getText().toString();

      //  profileImage = profileImageUrl;


        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String UserUid = mAuth.getCurrentUser().getUid();

        user User = new user(UserUid,userName,email,profileImageUrl);

        mDatabase.child(UserUid).setValue(User);

        }


    private void Register() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Sending Verification Email");

        TextView mailSignup = findViewById(R.id.mailSignup);
        String Email = mailSignup.getText().toString();

        TextView passwordSignup = findViewById(R.id.passwordSignup);
        String Password = passwordSignup.getText().toString();

        TextView nameSignup = findViewById(R.id.nameSignup);
        String Name = nameSignup.getText().toString();

        TextView ErrorText = findViewById(R.id.errortext);

        TextView confirmPassSignup = findViewById(R.id.confirmpassSignup);
        String Verefication = confirmPassSignup.getText().toString();


        if (!(Password.isEmpty()) && !(Email.isEmpty()) && !(Name.isEmpty()) && Password.equals(Verefication) && Password.length() > 5) {
            ErrorText.setVisibility(View.INVISIBLE);
            progressDialog.show();
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        saveUserInformation();
                      //  Toast.makeText(SignUp.this, "Save Done", Toast.LENGTH_SHORT).show();
                        sendVerificationEmail();

                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 1000);
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (Password.length() < 6) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
                // If sign in fails, display a message to the user.
                Toast.makeText(SignUp.this, "Password should be more than 5 caracters", Toast.LENGTH_SHORT).show();
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
                // If sign in fails, display a message to the user.
                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                ErrorText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sendVerificationEmail() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Sending Verification Email");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        currentUser.sendEmailVerification().addOnCompleteListener(SignUp.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                    mAuth.signOut();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




}
