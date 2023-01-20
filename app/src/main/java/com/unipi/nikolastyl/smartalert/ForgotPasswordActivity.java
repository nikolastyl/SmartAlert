package com.unipi.nikolastyl.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email=findViewById(R.id.editTextTextEmailAddress);
        mAuth=FirebaseAuth.getInstance();
    }

    public void goRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));

    }

    public void submit(View v){
        mAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Email send", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}