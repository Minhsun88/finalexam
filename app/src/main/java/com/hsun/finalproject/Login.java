package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsun.finalproject.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkUser();
        setTitle("多益小幫手");
        mAuth.signOut();

        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.tx1.getText().toString();
                String pwd = binding.tx2.getText().toString();

                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String loginEmail = authResult.getUser().getEmail();
                                Toast.makeText(Login.this, "登入成功:" + loginEmail,
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.putExtra("email",email);
                                intent.setClass(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "登入成功", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Login.this, register.class);
                startActivity(intent);

            }
        });

    }


    void checkUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name =  user.getEmail();
            binding.tx3.setText(name);

        }else binding.tx3.setText("尚未登入");
    }

}