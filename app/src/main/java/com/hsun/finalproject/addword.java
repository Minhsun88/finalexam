package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsun.finalproject.databinding.ActivityAddwordBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addword extends AppCompatActivity {
    private ActivityAddwordBinding B;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B=ActivityAddwordBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        setTitle("新增單字");
        ArrayList<String> wordL=new ArrayList<>();
        ArrayList<String> transL=new ArrayList<>();
        ArrayList<String> typeL=new ArrayList<>();

        Intent it =getIntent();
        String e=it.getStringExtra("email");

        db.collection("words")
                .document(e)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                ArrayList<String> ReadW = (ArrayList<String>) doc.get("word");
                                ArrayList<String> tranW = (ArrayList<String>) doc.get("trans");
                                ArrayList<String> typeW = (ArrayList<String>) doc.get("type");
                                for (int i = 0; i < ReadW.size(); i++) {
                                    wordL.add(ReadW.get(i));
                                    transL.add(tranW.get(i));
                                    typeL.add(typeW.get(i));
                                }
                            }
                        }
                    });


        B.ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Map < String, Object > addw = new HashMap<>();

                wordL.add(B.word.getText().toString());
                transL.add(B.trans.getText().toString());
                typeL.add(B.type.getText().toString());

                addw.put("word", wordL);
                addw.put("trans", transL);
                addw.put("type", typeL);

                db.collection("words")
                        .document(e)
                        .set(addw)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(addword.this,"輸入成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Demo", e.getMessage());
                            }
                        });
                Intent it = new Intent(addword.this,MainActivity.class);
                it.putExtra("email",e);
                startActivity(it);
            }
        });

        }
}