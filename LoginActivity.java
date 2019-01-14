package com.example.cristian.mentorme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{

    private EditText mEmail;
    private EditText mPassword;
    private CardView mButton;

    private TextView mRegister;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);
        mButton = (CardView) findViewById(R.id.login_button);
        mRegister = (TextView) findViewById(R.id.login_register_here);

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent goToLogin = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goToLogin);
            }
        });



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
                {
                    signInWithEmailAndPassword(email, password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill the form and try again.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void signInWithEmailAndPassword(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    goToMainActivity();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong password or email.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void goToMainActivity()
    {
        Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainActivity);
        finish();
    }
}
