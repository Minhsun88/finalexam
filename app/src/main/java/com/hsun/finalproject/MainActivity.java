package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsun.finalproject.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding B;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        Intent it =getIntent();
        String e=it.getStringExtra("email");
        setTitle("多益小幫手:"+e);
        db.collection("words")
                .document(e)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc=task.getResult();
                            ArrayList<String> ReadW =(ArrayList<String>)doc.get("word");
                            ArrayAdapter<String> Adpt=
                                    new ArrayAdapter<String>(MainActivity.this,
                                            android.R.layout.simple_list_item_1,ReadW);
                            B.Lst.setAdapter((Adpt));
                        }
                    }
                });



        B.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent();
                it.setClass(MainActivity.this,addword.class);
                it.putExtra("email",e);
                startActivity(it);

            }
        });
        B.Lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.collection("words")
                        .document(e)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc=task.getResult();
                                    ArrayList<String> ReadW =(ArrayList<String>)doc.get("word");
                                    ArrayList<String> ReadTrans =(ArrayList<String>)doc.get("trans");
                                    ArrayList<String> ReadType =(ArrayList<String>)doc.get("type");

                                    Intent it=new Intent();
                                    it.setClass(MainActivity.this,wordset.class);
                                    it.putExtra("W",ReadW.get(i));
                                    it.putExtra("Tr",ReadTrans.get(i));
                                    it.putExtra("Ty",ReadType.get(i));
                                    it.putExtra("num",String.valueOf(i));
                                    it.putExtra("email",e);
                                    startActivity(it);
                                }
                            }
                        });
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Intent i;
        switch (id)
        {
            case R.id.item1:
                i = new Intent(MainActivity.this,map.class);
                startActivity(i);
                break;
            case R.id.item2:
                i = new Intent(MainActivity.this,note.class);
                startActivity(i);

                break;
            case R.id.item3:
                mAuth.signOut();
                i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}