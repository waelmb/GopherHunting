package com.example.huntthegopher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToContinuous(View v) {
        //Create an Intent to start the Activity
        Intent intent = new Intent(MainActivity.this, board_continuous.class);

        // Start the Activity
        startActivity(intent);
    }

    public void goToGuessByGuess(View v) {
        //Create an Intent to start the Activity
        Intent intent = new Intent(MainActivity.this, board_guessbyguess.class);

        // Start the Activity
        startActivity(intent);
    }



}