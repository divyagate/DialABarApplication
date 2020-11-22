package com.example.dialabarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPassword;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputEmail = (EditText) findViewById(R.id.register_email_input);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = InputName.getText().toString().trim();
                String uemail = InputEmail.getText().toString().trim();
                String upassword = InputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(uemail)){
                    InputEmail.setError("Email is required");

                }
                if(TextUtils.isEmpty(uname)){
                    InputName.setError("Name is required");
                }
                if(TextUtils.isEmpty(upassword)){
                    InputPassword.setError("Please enter password");

                }
                if(upassword.length() < 6){
                    InputPassword.setError("Password must be more then 6 characters");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(uemail, upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,"User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}