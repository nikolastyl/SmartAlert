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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText email,phone,password,confirmPassword;
    Button registerBtn;
    String phonePattern= "(69)[0-9]{8}";
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database=FirebaseDatabase.getInstance();
        registerBtn=findViewById(R.id.registerBtn);
        email=findViewById(R.id.editTextTextEmail);
        phone=findViewById(R.id.editTextPhone2);
        password=findViewById(R.id.editTextTextPassword3);
        confirmPassword=findViewById(R.id.editTextTextPassword2);
        mAuth=FirebaseAuth.getInstance();
        dataRef=database.getReference();

    }


    public void goLogin(View v){
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

    }

    public void registerBtn(View view){
        String email1=email.getText().toString();
        String password1=password.getText().toString();
        String confirmPassword1=confirmPassword.getText().toString();
        String phone1=phone.getText().toString();
        final String[] uID = new String[1];
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("email",email1);
        hashMap.put("phone",phone1);
        if(!email1.matches(emailPattern)){
            email.setError("invalid email");
        }else if(!phone1.matches(phonePattern)){
            phone.setError("invalid number");
        }else if(password1.isEmpty()||password1.length()<8){
            password.setError("at least 8 characters");
        }else if(!(password1.equals(confirmPassword1))){
            confirmPassword.setError("passwords do not match");
        }else{
        mAuth.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            uID[0] =mAuth.getUid();
                            dataRef.child("Users")
                                    .child(uID[0])
                                    .setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task2) {
                                            if(task2.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "it's ok", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(RegisterActivity.this,task2.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });




        }

    }


}