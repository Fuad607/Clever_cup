package com.example.fuad.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by Fuad on 12/8/2016.
 */

public class SplashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_TIME= 800;
    private FirebaseAuth firebaseAuth;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle splash) {
        super.onCreate(splash);
        setContentView(R.layout.splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                firebaseAuth=FirebaseAuth.getInstance();
                if(firebaseAuth.getCurrentUser() != null){

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else{
                Intent splashIntent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(splashIntent);
                finish();}
            }
        }, SPLASH_TIME);
    }
}

