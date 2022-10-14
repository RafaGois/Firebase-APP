package com.example.firebase_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private String TAG = "AAA";

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private EditText inputEmail;
    private EditText inputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);

        auth.signOut();


        DatabaseReference usuarios = reference.child("usuarios");

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("FIREBASE" ,snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
/*

        Usuario usuario = new Usuario();
        usuario.setNome("José");
        usuario.setSobrenome("Becker");
        usuario.setIdade(50);


        usuarios.push().setValue(usuario);
*/

        recuperandoUsers();
    }

    public void logar (View view) {
        if (auth.getCurrentUser() == null) {

            String email = inputEmail.getText().toString();
            String senha = inputSenha.getText().toString();

            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Usuario logado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao tentar logar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void registrar (View view) {
        Intent intent = new Intent(this,Registrar.class);
        startActivity(intent);
    }

    private void recuperandoUsers () {

        DatabaseReference usuarios = reference.child("usuarios");
        //DatabaseReference userPesquisa = usuarios.child("-NEGN6NvDeNJctZXiFDA");

        //busca os valores respectivos do valor que tiver o nome de sandra
        //Query userPesquisa = usuarios.orderByChild("nome").equalTo("Sandra");

        //busca os primeiros tres registros
        Query userPesquisa = usuarios.orderByKey().limitToFirst(3);

        userPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Dados user", snapshot.getValue().toString());
                //atribuindo os valores do bano de dados a um objeto
                //Usuario usuarioDados = snapshot.getValue(Usuario.class);
                //Log.i("Dados",usuarioDados.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}