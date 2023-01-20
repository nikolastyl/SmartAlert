package com.unipi.nikolastyl.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    private FirebaseAuth mAuth;
    String email1,password1;
    String emailPatternForEmployers="[a-zA-Z0-9._-]+@(smartalert.gr)";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.editTextEmail1);
        password=findViewById(R.id.editTextTextPassword);
        mAuth=FirebaseAuth.getInstance();
        


    }

    public void goRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));

    }

    public void forgotPass(View v){
        startActivity(new Intent(this, ForgotPasswordActivity.class));

    }





    public void loginBtn(View v){

        email1=email.getText().toString();
        password1=password.getText().toString();
        mAuth.signInWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "successful login", Toast.LENGTH_SHORT).show();
                            if(email1.matches(emailPatternForEmployers)){
                                Toast.makeText(MainActivity.this, "he is an employ", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println(task.getException());

                        }

                    }
                });



    }


}