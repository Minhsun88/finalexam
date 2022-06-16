package com.hsun.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hsun.finalproject.databinding.ActivityNoteBinding;

public class note extends AppCompatActivity {

    private ActivityNoteBinding ABinding;
    private StorageReference storageRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("筆記區");
        storageRef = FirebaseStorage.getInstance().getReference();
        ABinding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(ABinding.getRoot());
        ABinding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("image/*");
                startActivityForResult(picker,101);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==101){
            Uri uri = data.getData();
            Glide.with(note.this)
                    .load(uri)
                    .into(ABinding.imageView);
            storageRef.child("test").child(uri.getLastPathSegment())
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(note.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
