package com.example.nicho.firebaseauthenticationtutorial;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterFragment extends Fragment implements View.OnClickListener
    {
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


        public RegisterFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.register_fragment,container,false);
                InitializeViews();
                firebaseAuth = FirebaseAuth.getInstance();

                return view;
            }

        private void InitializeViews()
            {
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
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.register_back_button:
                            getActivity().finish();
                            break;
                        case R.id.register_confirm_button:
                            RegisterUser();
                            break;
                    }
            }


        private void RegisterUser()
            {
                //todo
                //Log username together with other details

                mProgressBar.setVisibility(View.VISIBLE);

                String username = mUsernameEditText.getText().toString().trim();
                String emailAddress = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                boolean validPassword = ClientSideValidation.ValidatePassword(password, mPasswordLayout, mPasswordEditText);
                boolean validEmail = ClientSideValidation.ValidateEmailAddress(emailAddress, mEmailLayout,mEmailEditText);
                boolean validUsername = ClientSideValidation.ValidateUsername(username, mUsernameLayout, mUsernameEditText);

                if (validEmail == true && validPassword == true && validUsername == true)
                    {
                        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>()
                                    {
                                        @Override
                                        public void onSuccess(AuthResult authResult)
                                            {
                                                mProgressBar.setVisibility(View.GONE);
                                                Toasty.success(getActivity(),"Account registration successful!",Toast.LENGTH_SHORT, true).show();

                                                mUsernameEditText.setText("");
                                                mEmailEditText.setText("");
                                                mPasswordEditText.setText("");
                                                mUsernameEditText.requestFocus();

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable()
                                                    {
                                                        @Override
                                                        public void run()
                                                            {
                                                                getActivity().finish();
                                                            }
                                                    }, 1000);
                                            }
                                    })
                                .addOnFailureListener(getActivity(), new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                            {
                                                mProgressBar.setVisibility(View.GONE);
                                                Toasty.warning(getActivity(),e.getLocalizedMessage(), Toast.LENGTH_SHORT,true).show();
                                                mEmailEditText.setText("");
                                                mPasswordEditText.setText("");
                                                mEmailEditText.requestFocus();
                                            }
                                    });
                    }
                else
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }
            }






    }
