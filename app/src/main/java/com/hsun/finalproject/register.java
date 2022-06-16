package com.hsun.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsun.finalproject.databinding.ActivityRegisterBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private ActivityRegisterBinding Binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());
        setTitle("註冊帳號密碼:");
        Binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Binding.ed1.getText().toString();
                String pwd = Binding.ed2.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,pwd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String email =  authResult.getUser().getEmail();

                                Toast.makeText(register.this, "註冊成功:" +email ,
                                        Toast.LENGTH_SHORT).show();
                                Map< String, Object > addw = new HashMap<>();
                                ArrayList<String> wordL=new ArrayList<>();
                                ArrayList<String> transL=new ArrayList<>();
                                ArrayList<String> typeL=new ArrayList<>();

                                addw.put("word", wordL);
                                addw.put("trans", transL);
                                addw.put("type", typeL);

                                db.collection("words")
                                        .document(email)
                                        .set(addw);
                                Intent intent = new Intent();
                                intent.setClass(register.this, Login.class);
                                startActivity(intent);
                            }
                        });

            }

        });
    }


}
