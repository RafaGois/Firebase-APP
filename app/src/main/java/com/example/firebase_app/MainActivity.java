package com.example.firebase_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String TAG = "AAA";

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText inputEmail;
    private EditText inputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);

        auth.signOut();

/*
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


        Usuario usuario = new Usuario();
        usuario.setNome("Sandra");
        usuario.setSobrenome("Fontana");
        usuario.setIdade(48);



        usuarios.push().setValue(usuario);
*/
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

}