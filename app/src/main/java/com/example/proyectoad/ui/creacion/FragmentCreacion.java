package com.example.proyectoad.ui.creacion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.example.proyectoad.User;
import com.example.proyectoad.databinding.FragmentCreacionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FragmentCreacion extends Fragment {

//    private static final String CHANNEL_ID = "simple_notification_channel";
    private FragmentCreacionBinding binding;
    private Uri imagen;
    private StorageReference storageReference;
    private DatabaseReference incidenciasReference;
    private DatabaseReference usuariosReference;
    private FirebaseAuth mAuth;
    private static int idIncidencia;

    private final ActivityResultLauncher<Intent> galeryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if(selectedImage != null){
                        Picasso.get()
                                .load(selectedImage)
                                .placeholder(R.drawable.baseline_image_24).fit()
                                .error(R.drawable.baseline_image_24)
                                .into(binding.imgAddIncidencia);
                        imagen = selectedImage;
                    } else {
                        Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreacionBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("incidencias");
        incidenciasReference = FirebaseDatabase.getInstance().getReference("incidencias");
        usuariosReference = FirebaseDatabase.getInstance().getReference("usuarios");
        encontrarUltimoID();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgAddIncidencia.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            galeryLauncher.launch(intent);
        });

        binding.btnGuardar.setOnClickListener(v-> {
            String titulo = binding.etCreacionTitulo.getText().toString();
            String descripcion = binding.etCreacionDescripcion.getText().toString();

            if(!titulo.isEmpty()){
                if(!descripcion.isEmpty()){
                    if(imagen != null){
                        binding.fondoOscuro.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        StorageReference archivo = storageReference.child(titulo + "_" + idIncidencia + ".jpg");
                        archivo.putFile(imagen).addOnSuccessListener(taskSnapshot -> {
                            archivo.getDownloadUrl().addOnSuccessListener(uri -> {
                                String url = uri.toString();
                                Incidencia incidencia = new Incidencia(
                                        idIncidencia,
                                        titulo,
                                        descripcion,
                                        "",
                                        url,
                                        "Sin Revisar"
                            );
                            incidenciasReference.child(String.valueOf(idIncidencia)).setValue(incidencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        binding.etCreacionTitulo.setText("");
                                        binding.etCreacionDescripcion.setText("");
                                        binding.imgAddIncidencia.setImageURI(Uri.parse(""));
                                        aniadirIncidenciaAlUsuario();
                                        Toast.makeText(getContext(), R.string.fragment_creacion_incidencia_registrada_exito, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), R.string.fragment_creacion_incidencia_fallo, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            binding.fondoOscuro.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    );
                });
                    } else {
                        Toast.makeText(getContext(), R.string.fragment_creacion_no_hay_imagen, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.fragment_creacion_no_hay_descripcion, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), R.string.fragment_creacion_no_hay_titulo, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void aniadirIncidenciaAlUsuario() {
        String email = mAuth.getCurrentUser().getEmail().split("@")[0].replace(",","");
        usuariosReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User(
                        snapshot.child("nombre").getValue(String.class),
                        snapshot.child("apellidos").getValue(String.class),
                        snapshot.child("correo").getValue(String.class),
                        null,
                        snapshot.child("incidencias").getValue(String.class),
                        snapshot.child("telefono").getValue(String.class),
                        snapshot.child("piso_letra").getValue(String.class),
                        snapshot.child("foto_perfil").getValue(String.class),
                        snapshot.child("es_admin").getValue(Boolean.class)
                );
                if(user.getIncidencias().isEmpty()){
                    user.setIncidencias(String.valueOf(idIncidencia));
                } else {
                    user.setIncidencias(user.getIncidencias() + "," + idIncidencia);
                }
                usuariosReference.child(email).setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void encontrarUltimoID() {
        incidenciasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    idIncidencia = Integer.parseInt(data.getKey());
                }
                idIncidencia++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}