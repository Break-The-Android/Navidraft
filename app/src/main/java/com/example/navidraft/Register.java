package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class Register extends AppCompatActivity {

    EditText DName;
    EditText UName;
    EditText PWord;
    EditText CPWord;
    Button SignUp;
    String parameter;
    String x = "";

    FirebaseStorage FireStorage;
    StorageReference storageReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public Uri getImageUri(Context inContext, Bitmap inImage, String Title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, Title, null);
        return Uri.parse(path);
    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        DName=findViewById(R.id.DNAMETXT);
        UName=findViewById(R.id.RegUsernameTXT);
        PWord=findViewById(R.id.RegPasswordTXT);
        CPWord=findViewById(R.id.RegConfirmPasswordTXT);
        SignUp=findViewById(R.id.SignUpBTN);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign Up Logic

                if(DName.getText().toString().isEmpty() || UName.getText().toString().isEmpty() || PWord.getText().toString().isEmpty() || CPWord.getText().toString().isEmpty()){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                    tv.setText("All Fields Are Required");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }else{
                    // Second Loop for password verification
                    if(PWord.getText().toString().equals(CPWord.getText().toString())){
                        // Sign up functionality after checks

                        final String mEmail = UName.getText().toString().trim().toLowerCase();
                        final String mPass = PWord.getText().toString().trim();
                        final String MDisplay = DName.getText().toString().trim();

                        final int min = 1;
                        final int max = 3000;
                        final int random = new Random().nextInt((max - min) + 1) + min;

                        if(TextUtils.isEmpty(mEmail)){
                            UName.setError("Required Field...");
                            return;
                        }
                        if(TextUtils.isEmpty(mPass)){
                            PWord.setError("Required Field...");
                        }

                        // Fire base User Addition via email and password
                        mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    x = encodeUserEmail(mEmail);

                                    DataStructure ds = new DataStructure(random, MDisplay, mEmail, mPass, "--", "Metric", "Driving", "null", "Classic");

                                    StorageReference ref = FirebaseStorage.getInstance().getReference();
                                    Uri urix = Uri.parse("android.resource:com.example.navidraft/drawable/placeholderx");
                                    try {
                                        InputStream stream = getContentResolver().openInputStream(urix);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    // Clear fields
                                    DName.setText("");
                                    UName.setText("");
                                    PWord.setText("");
                                    CPWord.setText("");

                                    ref.child(x).putFile(urix).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                    mDatabase.child(x).setValue(ds);
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this);
                                    startActivity(new Intent(getApplicationContext(), HomeMap.class), options.toBundle());
                                }else{
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                                    tv.setText("Registration Unsuccessful");
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();                                }
                            }
                        });
                    }else{
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                        TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                        tv.setText("Passwords Must Match");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}