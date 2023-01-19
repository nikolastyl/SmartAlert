package com.unipi.nikolastyl.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText email,phone,password,confirmPassword;
    Button registerBtn;
    String phonePattern= "(69)[0-9]{8}";
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    FirebaseAuth mAuth;
    //FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn=findViewById(R.id.registerBtn);
        email=findViewById(R.id.editTextTextEmail);
        phone=findViewById(R.id.editTextPhone2);
        password=findViewById(R.id.editTextTextPassword3);
        confirmPassword=findViewById(R.id.editTextTextPassword2);
        mAuth=FirebaseAuth.getInstance();

    }


    public void goLogin(View v){
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

    }

    public void registerBtn(View view){
        String email1=email.getText().toString();
        String password1=password.getText().toString();
        String confirmPassword1=confirmPassword.getText().toString();
        String phone1=phone.getText().toString();
        System.out.println("mia kara");

     /*   if(!email1.matches(emailPattern)){
            email.setError("invalid email");
        }else if(!phone1.matches(phonePattern)){
            phone.setError("invalid number");
        }else if(password1.isEmpty()||password1.length()<8){
            password.setError("at least 8 characters");
        }else if(!(password1.equals(confirmPassword1))){
            confirmPassword.setError("passwords do not match");
        }else{
            System.out.println("ftasame kai do---------------");*/
        mAuth.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "it's ok", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });




       // }

    }


}