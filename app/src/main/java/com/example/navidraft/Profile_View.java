package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.MapStyleOptions;
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

import org.w3c.dom.Text;

public class Profile_View extends AppCompatActivity {

    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    private static final int PICK_IMAGE = 1;
    String FinalUri = "";
    String parameter;
    ImageButton Return;
    TextView DisplayName;
    TextView Email;
    TextView Password;
    TextView PhoneNumber;
    ImageButton ImageB;
    FloatingActionButton ChangePic;
    LinearLayout DisplayClick;
    LinearLayout EmailClick;
    LinearLayout PasswordClick;
    LinearLayout PhoneNumberClick;

    public static String FinalDisplayName = "";
    public static String FinalEmail = "";
    public static String FinalPhoneNumber = "";
    public static String FinalPassword = "";
    public static String FinalID = "";
    public static String FinalIMAGEURL = "";
    public static String FINALTRAVELTYPE = "";
    public static String FINALUNIT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__view);

        DisplayName = findViewById(R.id.Edit_Profile_DisplayName_TXT);
        Email = findViewById(R.id.Edit_Profile_Email_TXT);
        Password = findViewById(R.id.Edit_Profile_Password_TXT);
        PhoneNumber = findViewById(R.id.Edit_Profile_PhoneNumber_TXT);
        ImageB = findViewById(R.id.Edit_Profile_Picture_BTN);
        ChangePic = findViewById(R.id.fabchangepic);
        Return = findViewById(R.id.ProfileView_PreviousInterfaceBTN);

        DisplayClick = findViewById(R.id.DisplayNameClick_Layout);
        EmailClick = findViewById(R.id.EmailAddressClick_Layout);
        PasswordClick = findViewById(R.id.PasswordClick_Layout);
        PhoneNumberClick = findViewById(R.id.PhoneNumberClick_Layout);

        FinalDisplayName = HomeMap.FinalDisplayName;
        FinalEmail = HomeMap.FinalEmail;
        FinalPassword = HomeMap.FinalPassword;
        FinalPhoneNumber = HomeMap.FinalPhoneNumber;
        FINALTRAVELTYPE = HomeMap.FINALTRAVELTYPE;
        FINALUNIT = HomeMap.FinalDisplayName;
        FinalID = HomeMap.FinalID;

        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))) {
                    String DISPLAY = dataSnapshot.child(parameter).child("dname").getValue().toString();
                    String MAIL = dataSnapshot.child(parameter).child("uname").getValue().toString();
                    String PASSWORD = dataSnapshot.child(parameter).child("pword").getValue().toString();
                    String PHONE = dataSnapshot.child(parameter).child("phone").getValue().toString();
                    String GETIMGURL = dataSnapshot.child(parameter).child("imageURL").getValue().toString();

                    // Mask Password Using REGEX
                    String Encrypt = PASSWORD;
                    // Mask Password for Security Reasons
                    String y = Encrypt.replaceAll(".", "*");

                    // Set Text Fields
                    DisplayName.setText(DISPLAY);
                    Email.setText(MAIL);
                    PhoneNumber.setText(PHONE);
                    Password.setText(y);

                    // Refresh Image Placeholder with new Image on Data change
                    if (GETIMGURL == "null") {
                        // Do Nothing
                    } else {
                        Glide.with(getApplicationContext()).load(GETIMGURL).centerCrop().placeholder(getDrawable(R.drawable.account_foreground)).into(ImageB);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        });

        ChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        final Intent DisplaySwitch = new Intent(this, UpdateDisplayName.class);
        final Intent EmailSwitch = new Intent(this, UpdateEmailAddress.class);
        final Intent PasswordSwitch = new Intent(this, UpdatePassword.class);
        final Intent PhoneNumberSwitch = new Intent(this, UpdatePhoneNumber.class);

        DisplayClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DisplaySwitch);
                overridePendingTransition(R.anim.android_slidein, R.anim.stable);
            }
        });

        PhoneNumberClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PhoneNumberSwitch);
                overridePendingTransition(R.anim.android_slidein, R.anim.stable);
            }
        });

        EmailClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EmailSwitch);
                overridePendingTransition(R.anim.android_slidein, R.anim.stable);
            }
        });

        PasswordClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PasswordSwitch);
                overridePendingTransition(R.anim.android_slidein, R.anim.stable);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            final Uri ImageData = data.getData();

            if (resultCode == RESULT_OK)
            {
                if(ImageData != null){
                    final ProgressDialog progressDialog
                            = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading Image");
                    progressDialog.show();

                    final StorageReference ref
                            = storageReference;

                    ref.child(encodeUserEmail(encodeUserEmail(userauthnow.getEmail().toLowerCase()))).putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                            TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                            tv.setText("Image Uploaded Successfully");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                            ref.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FinalUri = uri.toString();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myref = database.getReference();

                                    myref.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("imageURL").setValue(FinalUri);
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error, Image not uploaded
                                    progressDialog.dismiss();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                                    tv.setText("Image Upload Unsuccessful");
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                            })

                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    public void SelectImage(){
        // Get images for Fire base Intent Instructions
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
}