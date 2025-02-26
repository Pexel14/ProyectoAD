package com.example.proyectoad;

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

                String nombre = binding.etNombre.getText().toString().trim();
                String apellidos = binding.etApellido.getText().toString().trim();
                String piso = binding.etPiso.getText().toString().trim();
                String telefono = binding.etTelefono.getText().toString().trim();

                if (nombre.isEmpty() || apellidos.isEmpty() || piso.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(FormUserActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else {

                    // TODO insertar los datos de usuario en su nodo



                }
            }
        });

    }



}