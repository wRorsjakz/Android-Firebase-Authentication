package com.example.nicho.firebaseauthenticationtutorial;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button mBackButton;
    private Button mConfirmButton;
    private ProgressBar mProgressBar;
    private TextInputLayout mUsernameLayout;
    private TextInputEditText mUsernameEditText;
    private TextInputLayout mEmailLayout;
    private TextInputEditText mEmailEditText;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mPasswordEditText;

    private FirebaseAuth firebaseAuth;


    public RegisterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment, container, false);
        InitializeViews();
        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void InitializeViews() {
        mBackButton = view.findViewById(R.id.register_back_button);
        mConfirmButton = view.findViewById(R.id.register_confirm_button);
        mProgressBar = view.findViewById(R.id.register_progressbar);
        mUsernameLayout = view.findViewById(R.id.register_username_textinputlayout);
        mUsernameEditText = view.findViewById(R.id.register_username_edittext);
        mEmailLayout = view.findViewById(R.id.register_email_textinputlayout);
        mEmailEditText = view.findViewById(R.id.register_email_edittext);
        mPasswordLayout = view.findViewById(R.id.register_password_textinputlayout);
        mPasswordEditText = view.findViewById(R.id.register_password_edittext);

        mBackButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back_button:
                getActivity().finish();
                break;
            case R.id.register_confirm_button:
                RegisterUser();
                break;
        }
    }


    private void RegisterUser() {
        //todo
        //Log username together with other details

        mProgressBar.setVisibility(View.VISIBLE);

        String username = mUsernameEditText.getText().toString().trim();
        String emailAddress = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        boolean validPassword = ClientSideValidation.ValidatePassword(password, mPasswordLayout, mPasswordEditText);
        boolean validEmail = ClientSideValidation.ValidateEmailAddress(emailAddress, mEmailLayout, mEmailEditText);
        boolean validUsername = ClientSideValidation.ValidateUsername(username, mUsernameLayout, mUsernameEditText);

        if (validEmail && validPassword && validUsername) {
            firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mProgressBar.setVisibility(View.GONE);

                            mUsernameEditText.setText("");
                            mEmailEditText.setText("");
                            mPasswordEditText.setText("");
                            mUsernameEditText.requestFocus();

                            sendEmailVerification();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().finish();
                                }
                            }, 2000);
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBar.setVisibility(View.GONE);
                            Toasty.warning(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                            mEmailEditText.setText("");
                            mPasswordEditText.setText("");
                            mEmailEditText.requestFocus();
                        }
                    });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private void sendEmailVerification() {
        try{
            firebaseAuth.getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toasty.success(getActivity(), "Email Verification sent", Toast.LENGTH_LONG, true).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }


}
