package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
    String FinalUri = "";
    ImageButton GoBack;
    CardView editprofileCard;
    CardView TripHistoryCard;
    CardView SettingsCard;
    FloatingActionButton LogoutFAB;
    TextView DName;
    TextView UName;
    TextView CPhone;
    ImageButton ProfilePic;
    String parameter;
    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static String FinalDisplayName = "";
    public static String FinalEmail = "";
    public static String FinalPhoneNumber = "";
    public static String FinalPassword = "";
    public static String FinalID = "";
    public static String FinalIMAGEURL = "";
    public static String FINALTRAVELTYPE = "";
    public static String FINALUNIT = "";
    public static String FINALTHEME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DName = findViewById(R.id.Profile_UNameLBL);
        UName = findViewById(R.id.Profile_EmailTXT);
        CPhone = findViewById(R.id.Profile_PhoneTXT);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();

        final FirebaseUser mUser = mAuth.getCurrentUser();
        final String UID = mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))){
                    String DISPLAY = dataSnapshot.child(parameter).child("dname").getValue().toString();
                    String MAIL = dataSnapshot.child(parameter).child("uname").getValue().toString();
                    String PASSWORD = dataSnapshot.child(parameter).child("pword").getValue().toString();
                    String PHONE = dataSnapshot.child(parameter).child("phone").getValue().toString();
                    String TRAVEL = dataSnapshot.child(parameter).child("travelType").getValue().toString();
                    String UNIT = dataSnapshot.child(parameter).child("unit").getValue().toString();
                    String URLData = dataSnapshot.child(parameter).child("imageURL").getValue().toString();
                    String theme = dataSnapshot.child(parameter).child("theme").getValue().toString();

                    DName.setText(DISPLAY);
                    UName.setText(MAIL);
                    CPhone.setText(PHONE);

                    FinalDisplayName = DISPLAY;
                    FinalEmail = MAIL;
                    FinalPassword = PASSWORD;
                    FinalPhoneNumber = PHONE;
                    FINALTRAVELTYPE = TRAVEL;
                    FINALUNIT = UNIT;
                    FinalIMAGEURL = URLData;
                    FINALTHEME = theme;

                    if(URLData == "null"){
                        // Do Nothing
                    }else {
                        Glide.with(getApplicationContext()).load(URLData).centerCrop().placeholder(getDrawable(R.drawable.account_foreground)).into(ProfilePic);
                    }
                }else{
                    // Do Nothing
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        GoBack = findViewById(R.id.PreviousInterfaceBTN);
        editprofileCard = findViewById(R.id.EditProfileCardView);
        TripHistoryCard = findViewById(R.id.TriphistoryCardView);
        SettingsCard = findViewById(R.id.SettingsCardView);
        LogoutFAB = findViewById(R.id.LogoutFAB);
        ProfilePic = findViewById(R.id.Profile_Picture_BTN);

        editprofileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this);
                 Intent intent = new Intent(Profile.this, Profile_View.class);
                 startActivity(intent, options.toBundle());
            }
        });

        TripHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this);
                Intent intent = new Intent(Profile.this, Destinations.class);
                startActivity(intent, options.toBundle());
            }
        });

        SettingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this);
                Intent intent = new Intent(Profile.this, Settings.class);
                startActivity(intent, options.toBundle());
            }
        });

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        });

        LogoutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                // Clear Stack Trace and all Stack Activities
                finishAffinity();

                // Introduce Fade Transition
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this);

                // Open Login Activity After Stack Trace is Empty
                startActivity(new Intent(Profile.this, Login.class), options.toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}