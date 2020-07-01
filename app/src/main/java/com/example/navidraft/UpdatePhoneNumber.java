package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdatePhoneNumber extends AppCompatActivity {

    ImageButton Return;
    EditText PhoneTXT;
    Button Update;

    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String parameter;

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_number);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        PhoneTXT = findViewById(R.id.UpdatePhoneNumberTXT);
        Return = findViewById(R.id.UpdatePhoneNumber_PreviousInterfaceBTN);
        Update = findViewById(R.id.UpdatePhoneNumberBTN);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))) {
                    String DISPLAY = dataSnapshot.child(parameter).child("phone").getValue().toString();
                    PhoneTXT.setText(DISPLAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        PhoneTXT.setText(Profile_View.FinalPhoneNumber);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update Logic
                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("phone").setValue(PhoneTXT.getText().toString());

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                tv.setText("Phone Number Updated");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();                finish();
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
        UpdatePhoneNumber.this.overridePendingTransition(0,R.anim.finish_slide_in);
    }
}
