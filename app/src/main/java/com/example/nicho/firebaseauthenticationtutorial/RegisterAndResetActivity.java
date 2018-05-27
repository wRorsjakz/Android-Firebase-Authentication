package com.example.nicho.firebaseauthenticationtutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.RelativeLayout;


public class RegisterAndResetActivity extends AppCompatActivity
    {
        private RelativeLayout relativeLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_register);

                relativeLayout = findViewById(R.id.register_containerlayout);

                MyFragmentManager();

            }

        private void MyFragmentManager()
            {
                Intent incomingIntent = getIntent();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                if(incomingIntent.hasExtra("REGISTRATION_FRAGMENT"))
                    {
                        RegisterFragment registerFragment = new RegisterFragment();
                        fragmentTransaction.replace(R.id.register_containerlayout, registerFragment, "Register Fragment").commit();
                    }
                else if(incomingIntent.hasExtra("RESET_PASSWORD_FRAGMENT"))
                    {
                        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                        fragmentTransaction.replace(R.id.register_containerlayout, resetPasswordFragment, "Reset Fragment").commit();
                    }
            }


    }
