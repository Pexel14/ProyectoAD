package com.example.proyectoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoad.databinding.ActivityRegitroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegitroBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");
        binding = ActivityRegitroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvVolver.setOnClickListener(v -> {
            volverAtras();
        });

        // Botón de registro
        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etNombre.getText().toString().trim();
                String email = binding.etCorreo.getText().toString().trim();
                String contrasenia = binding.etPassword.getText().toString().trim();
                String confirmPassword = binding.etPassword2.getText().toString().trim();


                if (username.isEmpty() || email.isEmpty() || contrasenia.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!contrasenia.equals(confirmPassword)) {
                    //TODO Mejorar aviso al usuario
                    Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (contrasenia.length() < 6) {
                    Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
                } else {
                    // Registrar usuario en Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, contrasenia)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        //String userId = firebaseUser.getUid();
                                        guardarUsuarioEnDatabase(username, email, contrasenia);

                                        Intent intent = new Intent(RegistroActivity.this, FormUserActivity.class);
                                        intent.putExtra("id", email);
                                        startActivity(intent);
                                        finish();

                                    }
                                } else {
                                    Toast.makeText(RegistroActivity.this, getString(R.string.registro_error_registro) + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

    }

    private void guardarUsuarioEnDatabase(String username, String email, String contrasenia) {

        String id = email.split("@")[0].replace(".", "");
        User user = new User(username, email, contrasenia);

        // Guardar en Firebase Realtime Database
        ref.child(id).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistroActivity.this, "El usuario ya está registrado", Toast.LENGTH_SHORT).show();

                binding.etCorreo.setText("");
                binding.etPassword.setText("");
                binding.etPassword2.setText("");
                binding.etNombre.setText("");
            } else {
                Toast.makeText(RegistroActivity.this, "Error inesperado al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Clic en iniciar sesion
    public void volverAtras() {
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
