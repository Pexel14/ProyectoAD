package com.example.proyectoad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoad.databinding.ActivityFormUserBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FormUserActivity extends AppCompatActivity {

    private ActivityFormUserBinding binding;
    private DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGuardarForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String apellidos = binding.etApellido.getText().toString().trim();
                String piso = binding.etPiso.getText().toString().trim();
                String telefono = binding.etTelefono.getText().toString().trim();

                if (apellidos.isEmpty() || piso.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(FormUserActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else {

                    // TODO insertar los datos de usuario en su nodo
                    ref = FirebaseDatabase.getInstance().getReference("usuarios");

                    String id = getIntent().getStringExtra("id").split("@")[0].replace(".", "");

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("apellidos", apellidos);
                    updates.put("piso", piso);
                    updates.put("telefono", telefono);

                    // XXX PROBLEMA EN LA IMAGEN

                    ref.child(id).updateChildren(updates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(FormUserActivity.this, "Formulario guardado correctamente", Toast.LENGTH_SHORT).show();

                            binding.etApellido.setText("");
                            binding.etPiso.setText("");
                            binding.etTelefono.setText("");

                            /*Intent intent = new Intent(FormUserActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); */

                        } else {
                            Toast.makeText(FormUserActivity.this, "Error inesperado al registrar", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

    }



}