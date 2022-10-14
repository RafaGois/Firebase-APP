package com.example.firebase_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Imagens extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagens);

        deletarImagem();

        baixarImagem();
    }

    public void clicouImagem (View view) {
        ImageView imagem = findViewById(R.id.fotoAndroid);

        //habilitando a possibilidade de gerar a imagem a partir da memoria
        imagem.setDrawingCacheEnabled(true);
        //gera memoria a partir de um bitmap
        imagem.buildDrawingCache();

        Bitmap bitmap = imagem.getDrawingCache();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //QUALIDADE DE 0 A 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,outputStream);

        byte[] dadosImagem = outputStream.toByteArray();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagens = storageReference.child("imagens");


        //gerando nome arquivo randomico
        String nomeArquivo = UUID.randomUUID().toString();
        StorageReference imgRef = imagens.child("nomeArquivo");

        UploadTask uploadTask = imgRef.putBytes( dadosImagem );

        uploadTask.addOnFailureListener(Imagens.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Imagens.this, "Erro ao tentar fazer upload do arquivo", Toast.LENGTH_SHORT).show();
            }
        });
        uploadTask.addOnSuccessListener(Imagens.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        Toast.makeText(Imagens.this, "SALVO COM SUCESSO" + uri.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deletarImagem () {
        ImageView imagem = findViewById(R.id.fotoAndroid);

        imagem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens = storageReference.child("imagens");
                StorageReference imagemRef = imagens.child("nomeArquivo");

                imagemRef.delete().addOnFailureListener(Imagens.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Imagens.this, "Falha ao apagar arquivo" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(Imagens.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Imagens.this, "Sucesso ao apagar ", Toast.LENGTH_LONG).show();
                    }
                });

                return true;
            }
        });
    }

    private void baixarImagem () {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagens = storageReference.child("imagens");
        StorageReference imagemRef = imagens.child("nomeArquivo");

        ImageView imagemRecebedora = findViewById(R.id.imagemRecebedora);

        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(Imagens.this).load(uri).into(imagemRecebedora);
                Toast.makeText(Imagens.this,"Sucesso ao buscar do server.", Toast.LENGTH_LONG).show();
            }
        });
    }
}