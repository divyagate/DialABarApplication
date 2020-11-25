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

import com.example.dialabarapplication.Model.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPassword;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String uname,uemail,upassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputEmail = (EditText) findViewById(R.id.register_email_input);
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = InputName.getText().toString().trim();
                uemail = InputEmail.getText().toString().trim();
                upassword = InputPassword.getText().toString().trim();

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
                            addData(uname,uemail,upassword);
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

    public void addData(String InputName, String InputEmail, String InputPassword) {
        Register register = new Register(InputName, InputEmail, InputPassword);
        db.collection("users")
                .add(register)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Users recorded", Toast.LENGTH_LONG).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



    }
}