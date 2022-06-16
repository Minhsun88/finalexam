package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.hsun.finalproject.databinding.ActivityWordsetBinding;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class wordset extends AppCompatActivity {

    private ActivityWordsetBinding B;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int N;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B=ActivityWordsetBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        setTitle("單字設定");
        Intent it = getIntent();
        String W = it.getStringExtra("W");
        String Tr = it.getStringExtra("Tr");
        String Ty = it.getStringExtra("Ty");
        String num=it.getStringExtra("num");
        String e=it.getStringExtra("email");
        N=Integer.parseInt(num);
        B.Edt1.setText(W);
        B.Edt2.setText(Tr);
        B.Edt3.setText(Ty);

        ArrayList<String> wordL=new ArrayList<>();
        ArrayList<String> transL=new ArrayList<>();
        ArrayList<String> typeL=new ArrayList<>();

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

        B.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map< String, Object > addw = new HashMap<>();
                String[] wS = wordL.toArray(new String[]{});
                wS[N]=B.Edt1.getText().toString();

                String[] trS = transL.toArray(new String[]{});
                trS[N]=B.Edt2.getText().toString();

                String[] tyS = typeL.toArray(new String[]{});
                tyS[N]=B.Edt3.getText().toString();

                ArrayList<String> NwordL=new ArrayList<>();
                ArrayList<String> NtransL=new ArrayList<>();
                ArrayList<String> NtypeL=new ArrayList<>();
                for (int i = 0; i < wS.length; i++) {
                    NwordL.add(wS[i]);
                    NtransL.add(trS[i]);
                    NtypeL.add(tyS[i]);
                }

                addw.put("word", NwordL);
                addw.put("trans", NtransL);
                addw.put("type", NtypeL);

                db.collection("words")
                        .document(e)
                        .set(addw)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(wordset.this,"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Demo", e.getMessage());
                            }
                        });
                Intent it = new Intent(wordset.this,MainActivity.class);
                it.putExtra("email",e);
                startActivity(it);
            }
        });

        B.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map< String, Object > addw = new HashMap<>();
                wordL.remove(N);
                transL.remove(N);
                typeL.remove(N);
                addw.put("word", wordL);
                addw.put("trans", transL);
                addw.put("type", typeL);

                db.collection("words")
                        .document(e)
                        .set(addw)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(wordset.this,"刪除成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Demo", e.getMessage());
                            }
                        });
                Intent it = new Intent(wordset.this,MainActivity.class);
                it.putExtra("email",e);
                startActivity(it);
            }
        });
        B.code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = wordL.get(N);
                try{
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE,400,400);
                    B.img.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
