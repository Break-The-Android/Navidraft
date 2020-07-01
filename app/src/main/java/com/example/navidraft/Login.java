package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }


    private FirebaseAuth mAuth;

    EditText UName;
    EditText PWord;
    Button Log;
    TextView SignupTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        UName=findViewById(R.id.UsernameTXT);
        PWord=findViewById(R.id.PasswordTXT);
        Log=findViewById(R.id.LoginBTN);
        SignupTV=findViewById(R.id.SignUpTV);

        // Testing Purposes
        UName.setText("mckaylanperumalsami7@gmail.com");
        PWord.setText("111111");

        final Intent intentReg = new Intent(this, Register.class);

        SignupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(intentReg);
            }
        });

        Log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Login Logic

                final String mEmail=UName.getText().toString().trim();
                final String mPass=PWord.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    UName.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(mPass)){
                    PWord.setError("Required Field");
                    return;
                }

                String input = UName.getText().toString().toLowerCase();

                mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this);
                            startActivity(new Intent(getApplicationContext(),HomeMap.class), options.toBundle());
                        }else{
                            Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Close Application to prevent splash screen from displaying on the back press
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}