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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    //UI
    private EditText mEmail;

    private EditText mPassword;
    private EditText mPassword_2;
    private CardView mButton;
    private TextView mLogin;

    //Firebase
    private DatabaseReference mUsers;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.register_email);

        mPassword = (EditText) findViewById(R.id.register_password);
        mPassword_2 = (EditText) findViewById(R.id.register_password_2);

        mLogin = (TextView) findViewById(R.id.register_login_here);
        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent goToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(goToLogin);

            }
        });



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mButton = (CardView) findViewById(R.id.register_button);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String password2 = mPassword_2.getText().toString();



                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2))
                  if (password.equals(password2))

                        createAccount(email, password);
                    else
                        Toast.makeText(getApplicationContext(), "Passwords should match.", Toast.LENGTH_SHORT).show();

                 else
                    Toast.makeText(getApplicationContext(), "Please fill the form and try again.", Toast.LENGTH_SHORT).show();




            }
        });
    }

    private void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = current_user.getUid();

                    mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
                    HashMap<String, String>hashMap = new HashMap<>();
                    hashMap.put("firstname", "null");
                    hashMap.put("secondname", "null");
                    hashMap.put("image", "null");
                    hashMap.put("specialization", "null");
                    hashMap.put("semester", "-1");
                    hashMap.put("status", "I don't know");
                    hashMap.put("biography", "null");
                    mUsers.setValue(hashMap);
                    goToCreateAccount();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Email already registered.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToCreateAccount()
    {
        Intent goToMainActivity = new Intent(getApplicationContext(), CreateProfile.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainActivity);
        finish();
    }
}
