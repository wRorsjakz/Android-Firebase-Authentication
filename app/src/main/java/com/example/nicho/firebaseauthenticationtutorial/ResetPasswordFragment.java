package com.example.nicho.firebaseauthenticationtutorial;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener
    {
        private View view;

        private TextInputLayout textInputLayout;
        private TextInputEditText textInputEditText;
        private Button confirmButton;
        private Button backButton;
        private ProgressBar progressBar;
        private FirebaseAuth firebaseAuth;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.reset_password_fragment, container, false);
                InitializeViews();

                firebaseAuth = FirebaseAuth.getInstance();

                confirmButton.setOnClickListener(this);
                backButton.setOnClickListener(this);

                return view;
            }

        private void InitializeViews()
            {
                textInputLayout = view.findViewById(R.id.reset_email_inputlayout);
                textInputEditText = view.findViewById(R.id.reset_email_edittext);
                confirmButton = view.findViewById(R.id.reset_confirm_button);
                backButton = view.findViewById(R.id.reset_back_button);
                progressBar = view.findViewById(R.id.reset_progressbar);
            }

        private void OnBackButtonPressed()
            {
                getActivity().finish();
            }

        private void OnConfirmButtonPressed()
            {
                progressBar.setVisibility(View.VISIBLE);
                final String email = textInputEditText.getText().toString().trim();
                boolean validEmail = ClientSideValidation.ValidateEmailAddress(email, textInputLayout, textInputEditText);

                if(validEmail)
                    {
                        firebaseAuth.sendPasswordResetEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Toasty.success(getActivity(), "Email sent to " + email, Toast.LENGTH_SHORT,true).show();
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
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        textInputEditText.setText("");
                                        textInputEditText.requestFocus();

                                        Toasty.warning(getActivity(), "Email address not found", Toast.LENGTH_SHORT, true).show();
                                    }
                            });
                    } else
                    {
                        progressBar.setVisibility(View.GONE);
                    }

            }

        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.reset_confirm_button:
                            OnConfirmButtonPressed();
                            break;
                        case R.id.reset_back_button:
                            OnBackButtonPressed();
                            break;
                    }
            }
    }
