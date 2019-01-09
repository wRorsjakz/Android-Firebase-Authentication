package com.example.nicho.firebaseauthenticationtutorial;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoardActivity extends AppCompatActivity
    {
        private Button mLogoutButton;
        private Button mDisableGoogleButton;

        private FirebaseAuth firebaseAuth;
        private GoogleSignInClient googleSignInClient;
        private  GoogleSignInOptions googleSignInOptions;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_dash_board);

                mLogoutButton = findViewById(R.id.dashboard_logout_button);
                mDisableGoogleButton = findViewById(R.id.dashboard_disable_google_button);

                googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(DashBoardActivity.this, googleSignInOptions);

                mLogoutButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                DoSignOut();
                            }
                    });

                mDisableGoogleButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                RevokeGoogleAccess();
                            }
                    });



            }

        private void DoSignOut()
            {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashBoardActivity.this, SignInActivity.class);
                startActivity(intent);
            }

        @Override
        protected void onStart()
            {
                super.onStart();
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser == null)
                    {
                        DoSignOut();
                    }
            }

        //Allow User to unlink their google account to the app
        private void RevokeGoogleAccess()
            {
                googleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                            {

                                Toast.makeText(DashBoardActivity.this, "Google Account disconnected", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DashBoardActivity.this, SignInActivity.class);
                                startActivity(intent);
                            }
                    });
            }
    }
