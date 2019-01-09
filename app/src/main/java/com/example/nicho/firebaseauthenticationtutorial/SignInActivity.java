package com.example.nicho.firebaseauthenticationtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailEditText;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordEdittext;
    private ProgressBar mProgressBar;
    private Button mLoginButton;
    private Button mForgotPasswordButton;
    private Button mRegisterButton;
    private SignInButton googleSignInButton;

    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        InitializeViews();

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            Toast.makeText(this, "Still Logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void InitializeViews() {
        mEmailLayout = findViewById(R.id.login_email_textinputlayout);
        mEmailEditText = findViewById(R.id.login_email_edittext);
        mPasswordLayout = findViewById(R.id.login_password_textinputlayout);
        mPasswordEdittext = findViewById(R.id.login_password_edittext);
        mProgressBar = findViewById(R.id.login_progressbar);
        mLoginButton = findViewById(R.id.login_login_button);
        mForgotPasswordButton = findViewById(R.id.login_forgot_password_button);
        mRegisterButton = findViewById(R.id.login_register_button);
        googleSignInButton = findViewById(R.id.login_google_button);

        mRegisterButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mForgotPasswordButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register_button:
                GoToRegistration();
                break;
            case R.id.login_login_button:
                DoEmailSignIn();
                break;
            case R.id.login_forgot_password_button:
                DoPasswordReset();
                break;
            case R.id.login_google_button:
                DoGoogleSignIn();
                break;

        }
    }

    private void DoEmailSignIn() {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEdittext.getText().toString().trim();

        boolean validEmail = ClientSideValidation.ValidateEmailAddress(email, mEmailLayout, mEmailEditText);
        boolean validPassword = EnsureThatPasswordIsNotEmpty(password);

        mProgressBar.setVisibility(View.VISIBLE);

        if (validEmail && validPassword) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(SignInActivity.this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mProgressBar.setVisibility(View.GONE);
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toasty.error(SignInActivity.this, "Please verify email address", Toast.LENGTH_SHORT
                                , true).show();
                        firebaseAuth.signOut();
                    }
                }
            }).addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressBar.setVisibility(View.GONE);
                    Toasty.warning(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void DoGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(SignInActivity.this, googleSignInOptions);
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);

    }

    private void GoToRegistration() {
        Intent intent = new Intent(SignInActivity.this, RegisterAndResetActivity.class);
        intent.putExtra("REGISTRATION_FRAGMENT", 1);
        startActivity(intent);
    }

    private void DoPasswordReset() {
        Intent intent = new Intent(SignInActivity.this, RegisterAndResetActivity.class);
        intent.putExtra("RESET_PASSWORD_FRAGMENT", 2);
        startActivity(intent);
    }

    private boolean EnsureThatPasswordIsNotEmpty(String _password) {
        mPasswordLayout.setErrorEnabled(false);
        if (_password.isEmpty()) {
            mPasswordLayout.setErrorEnabled(true);
            mPasswordLayout.setError("Please enter password");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Toasty.warning(SignInActivity.this, "Cancelled", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MYTAG", "onComplete: Google sign in successful");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Sign in sucessful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
