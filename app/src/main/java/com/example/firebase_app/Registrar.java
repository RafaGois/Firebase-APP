package com.example.firebase_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Registrar extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    EditText inputEmail;
    EditText inputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        inputEmail = findViewById(R.id.inputEmailRegister);
        inputSenha = findViewById(R.id.inputPasswordRegister);
    }

    public void registrar (View view) {

        String email = inputEmail.getText().toString();
        String senha = inputSenha.getText().toString();

        if (!email.isEmpty() && !senha.isEmpty()) {
            auth.createUserWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(Registrar.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registrar.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Registrar.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
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
        StorageReference imgRef = imagens.child(nomeArquivo);

        UploadTask uploadTask = imgRef.putBytes( dadosImagem );

        uploadTask.addOnFailureListener(Registrar.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registrar.this, "Erro ao tentar fazer upload do arquivo", Toast.LENGTH_SHORT).show();
            }
        });
        uploadTask.addOnSuccessListener(Registrar.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        Toast.makeText(Registrar.this, "SALVO COM SUCESSO" + uri.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}