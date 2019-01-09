package com.example.nicho.firebaseauthenticationtutorial;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Patterns;

import java.util.regex.Pattern;

public class ClientSideValidation
    {
        private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$");

        public static boolean ValidatePassword(String _password, TextInputLayout passwordLayout, TextInputEditText passwordEditText)
            {
                if (_password.isEmpty())
                    {
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("Please enter a password");
                        passwordEditText.requestFocus();
                        return false;
                    } else if ((!PASSWORD_PATTERN.matcher(_password).matches()))
                    {
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("Password too weak");
                        passwordEditText.requestFocus();
                        return false;
                    } else
                    {
                        passwordLayout.setErrorEnabled(false);
                    }
                return true;
            }

        public static boolean ValidateEmailAddress(String _EmailAddress, TextInputLayout emailLayout, TextInputEditText emailEditText)
        {
            if (_EmailAddress.isEmpty())
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Please enter email address");
                    emailEditText.requestFocus();
                    return false;
                }
            else if ((!Patterns.EMAIL_ADDRESS.matcher(_EmailAddress).matches()))
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Please enter a valid email address");
                    emailEditText.requestFocus();
                    return false;
                }
            else
                {
                    emailLayout.setErrorEnabled(false);
                }
            return true;
        }

        public static boolean ValidateUsername(String _username, TextInputLayout usernameLayout, TextInputEditText usernameEditText)
            {
                if(_username.isEmpty())
                    {
                        usernameLayout.setErrorEnabled(true);
                        usernameLayout.setError("Please enter username");
                        usernameEditText.requestFocus();
                        return false;
                    }
                else
                    {
                        usernameLayout.setErrorEnabled(false);
                    }
                return true;
            }
    }
