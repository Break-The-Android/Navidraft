package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navidraft.Model.DataStructure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class UpdateEmailAddress extends AppCompatActivity {

    ImageButton Return;
    EditText EmailTXT;
    Button Update;

    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email_address);

        mAuth=FirebaseAuth.getInstance();

        final String a = HomeMap.FinalDisplayName;
        final String b = HomeMap.FinalEmail;
        final String c = HomeMap.FinalPassword;
        final String d = HomeMap.FinalIMAGEURL;
        final String e = HomeMap.FinalPhoneNumber;
        final String f = HomeMap.FINALTRAVELTYPE;
        final String g = HomeMap.FINALUNIT;
        final String h = HomeMap.FINALTHEME;
        final String parameter;


        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        EmailTXT = findViewById(R.id.UpdateEmailAddressTXT);
        Return = findViewById(R.id.UpdateEmailAddress_PreviousInterfaceBTN);
        Update = findViewById(R.id.UpdateEmailAddressBTN);

        EmailTXT.setText(Profile_View.FinalEmail);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))) {
                    String MAIL = dataSnapshot.child(parameter).child("uname").getValue().toString();
                    EmailTXT.setText(MAIL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), HomeMap.FinalPassword);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateEmailAddress.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.reauthenticate, null);
                    dialogBuilder.setView(dialogView);

                    final EditText ReauthenticatePasswordTXT = (EditText) dialogView.findViewById(R.id.ReauthenticateEmailTXT);
                    Button Continue = (Button) dialogView.findViewById(R.id.ReauthenticateEmailBTN);

                    Continue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(HomeMap.FinalPassword.equals(ReauthenticatePasswordTXT.getText().toString())){
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                                        userauthnow.updateEmail(EmailTXT.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    // Remove Child Node
                                                    FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                                                    mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).removeValue();

                                                    // Create New Node
                                                    final int min = 1;
                                                    final int max = 3000;
                                                    final int random = new Random().nextInt((max - min) + 1) + min;

                                                    DataStructure ds = new DataStructure(random, a, b, c, e, g, f, d, h);
                                                    mDatabase.child(encodeUserEmail(EmailTXT.getText().toString().toLowerCase())).setValue(ds);

                                                    mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("uname").setValue(EmailTXT.getText().toString());

                                                    // Display Success Message
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                                    TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                                    tv.setText("Email Address Updated");
                                                    Toast toast = new Toast(getApplicationContext());
                                                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                                    toast.setDuration(Toast.LENGTH_LONG);
                                                    toast.setView(layout);
                                                    toast.show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }else{
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                                tv.setText("Invalid Credential");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                        }
                    });

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }catch (Exception f){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                    tv.setText(f.toString());
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.finish_slide_in);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        UpdateEmailAddress.this.overridePendingTransition(0,R.anim.finish_slide_in);
    }
}