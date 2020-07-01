package com.example.navidraft;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdatePassword extends AppCompatActivity {

    ImageButton Return;
    EditText PasswordTXT;
    Button Update;

    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        PasswordTXT = findViewById(R.id.UpdatePasswordTXT);
        Return = findViewById(R.id.UpdatePassword_PreviousInterfaceBTN);
        Update = findViewById(R.id.UpdatePasswordBTN);

        PasswordTXT.setText(Profile_View.FinalPassword);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                tv.setText("Password Updated");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
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
        UpdatePassword.this.overridePendingTransition(0,R.anim.finish_slide_in);
    }
}
