package com.example.navidraft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView TestConnTXT = findViewById(R.id.TestConnTV);
        final TextView CompTV = findViewById(R.id.CompTV);
        final TextView assets = findViewById(R.id.LaunchTV);

        progress=findViewById(R.id.progressBarSpinner);
        progress.setVisibility(View.INVISIBLE);
        TestConnTXT.setVisibility(View.INVISIBLE);
        CompTV.setVisibility(View.INVISIBLE);

        final Intent intentLogin = new Intent(this, Login.class);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                // execute the following when the timer is still active
                progress.setVisibility(View.VISIBLE);

                if((millisUntilFinished/1000) == 2){
                    TestConnTXT.setVisibility(View.VISIBLE);
                    CompTV.setVisibility(View.INVISIBLE);
                    assets.setVisibility(View.INVISIBLE);
                }
                else if((millisUntilFinished/1000) == 1){
                    TestConnTXT.setVisibility(View.INVISIBLE);
                    CompTV.setVisibility(View.VISIBLE);
                    assets.setVisibility(View.INVISIBLE);
                }
                else if((millisUntilFinished/1000) == 0){
                    TestConnTXT.setVisibility(View.INVISIBLE);
                    CompTV.setVisibility(View.INVISIBLE);
                    assets.setVisibility(View.VISIBLE);
                }
            }

            public void onFinish() {
                // Execute the following once the timer is complete
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(intentLogin, options.toBundle());
                progress.setVisibility(View.INVISIBLE);

                TestConnTXT.setVisibility(View.INVISIBLE);
                CompTV.setVisibility(View.INVISIBLE);
                assets.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
}