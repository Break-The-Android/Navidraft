package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateDisplayName extends AppCompatActivity {

    ImageButton Return;
    EditText DisplayTXT;
    Button Update;
    String parameter;

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
        setContentView(R.layout.activity_update_display_name);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        DisplayTXT = findViewById(R.id.UpdateDisplayNameTXT);
        Return = findViewById(R.id.UpdateDisplayName_PreviousInterfaceBTN);
        Update = findViewById(R.id.UpdateDisplayNameBTN);

        DisplayTXT.setText(Profile_View.FinalDisplayName);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))) {
                    String DISPLAY = dataSnapshot.child(parameter).child("dname").getValue().toString();
                    DisplayTXT.setText(DISPLAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update Logic
                FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("dname").setValue(DisplayTXT.getText().toString());

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                tv.setText("Display Name Updated");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                finish();
                overridePendingTransition(0, R.anim.finish_slide_in);
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
        UpdateDisplayName.this.overridePendingTransition(0,R.anim.finish_slide_in);
    }
}