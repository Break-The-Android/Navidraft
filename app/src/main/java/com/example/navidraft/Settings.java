package com.example.navidraft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Settings extends AppCompatActivity {

    ImageButton Return;
    FirebaseStorage FireStorage;
    StorageReference storageReference;
    MaterialCardView UnitSystem;
    MaterialCardView TravelModeSystem;
    MaterialCardView MapThemeSystem;
    MaterialCardView DeleteAccount;
    public static int getunit;
    public static int getmode;
    public static int gettheme;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        Return = findViewById(R.id.Settings_PreviousInterfaceBTN);

        UnitSystem = findViewById(R.id.UnitID);
        TravelModeSystem = findViewById(R.id.TravelModeCardID);
        MapThemeSystem = findViewById(R.id.MapStyleCardID);
        DeleteAccount = findViewById(R.id.DeleteAccountCardID);

        UnitSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] units = {"Metric", "Imperial"};

                if(Profile.FINALUNIT.equals("Imperial")){
                    getunit = 1;
                }else if(Profile.FINALUNIT.equals("Metric")){
                    getunit = 0;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Select Unit System");

                builder.setSingleChoiceItems(units,getunit , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("unit").setValue(checkedItem);

                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Metric System Enabled");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                        else if(item == 1){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("unit").setValue(checkedItem);
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Imperial System Enabled");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        TravelModeSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] units = {"Driving", "Walking"};

                if(Profile.FINALTRAVELTYPE.equals("driving")){
                    getmode = 0;
                }else if(Profile.FINALTRAVELTYPE.equals("walking")){
                    getmode = 1;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Select Travel Mode");
                builder.setSingleChoiceItems(units,getmode , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem2 = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("travelType").setValue(checkedItem2.toString().toLowerCase());
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Travel Mode Changed To " + checkedItem2.toString());
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                        else if(item == 1){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("travelType").setValue(checkedItem.toString().toLowerCase());
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Travel Mode Changed To " + checkedItem.toString());
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        MapThemeSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] units = {"Classic", "Midnight"};

                if(Profile.FINALTHEME.equals("Classic")){
                    gettheme = 0;
                }else if(Profile.FINALTHEME.equals("Midnight")){
                    gettheme = 1;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setTitle("Select Map Theme");
                builder.setSingleChoiceItems(units,gettheme , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem2 = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("theme").setValue(checkedItem2);
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Theme Changed To " + checkedItem2.toString());
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                        else if(item == 1){
                            ListView lw = ((AlertDialog)dialog).getListView();
                            Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
                            if(lw.getCheckedItemCount() > 0){
                                mDatabase.child(encodeUserEmail(userauthnow.getEmail().toLowerCase())).child("theme").setValue(checkedItem);
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                                tv.setText("Theme Changed To " + checkedItem.toString());
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                dialog.cancel();
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                tv.setText("Feature Coming Soon!");
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
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}