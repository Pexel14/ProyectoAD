package com.example.proyectoad.ui.perfil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentEditarPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class EditarPerfilFragment extends Fragment {

    private FragmentEditarPerfilBinding binding;
    private Uri imagen;
    private StorageReference storageReference;
    private DatabaseReference usuariosReference;

    private final ActivityResultLauncher<Intent> galeryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK && result.getData() == null) {
                    Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri selectedImage = result.getData().getData();

                if(selectedImage == null){
                    Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                Picasso.get()
                        .load(selectedImage)
                        .placeholder(R.drawable.baseline_image_24).fit()
                        .error(R.drawable.baseline_image_24)
                        .into(binding.imgPerfilUser);
                imagen = selectedImage;
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditarPerfilBinding.inflate(inflater, container, false );
        usuariosReference = FirebaseDatabase.getInstance().getReference("usuarios");
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userId = email.split("@")[0];
        storageReference = FirebaseStorage.getInstance().getReference("fotos_perfil");
        View root = binding.getRoot();

        View.OnClickListener listener = v -> abrirArchivos();
        binding.ibtnEdit.setOnClickListener(listener);
        binding.imgPerfilUser.setOnClickListener(listener);

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = binding.etNombre.getText().toString();
                String apellidos = binding.etApellido.getText().toString();
                String piso = binding.etPiso.getText().toString();
                String telefono = binding.etTelefono.getText().toString();

                if (nombre.isEmpty() || apellidos.isEmpty() || piso.isEmpty() || telefono.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuariosReference.child(userId).child("nombre").setValue(nombre);
                usuariosReference.child(userId).child("apellidos").setValue(apellidos);
                usuariosReference.child(userId).child("piso").setValue(piso);
                usuariosReference.child(userId).child("telefono").setValue(telefono);

                if(imagen != null){
                    // Alamacenamiento con storage
                    usuariosReference.child(userId).child("foto_perfil").setValue(imagen.toString());
                    StorageReference archivo = storageReference.child(userId + ".png");
                    archivo.putFile(imagen).addOnSuccessListener(taskSnapshot -> {
                        archivo.getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            usuariosReference.child(userId).child("foto_perfil").setValue(url);
                        });
                    });
                }

                volverAtras();

            }
        });

        View.OnClickListener lsnVolverAtras = v -> volverAtras();
        binding.btnCancelar.setOnClickListener(lsnVolverAtras);

        return root;
    }

    private void volverAtras() {
        Navigation.findNavController(getView()).popBackStack();
    }

    private void abrirArchivos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        galeryLauncher.launch(intent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PerfilViewModel model = new ViewModelProvider(this).get(PerfilViewModel.class);
        final ImageView ivPerfil = binding.imgPerfilUser;
        final TextView tvCorreo = binding.tvCorreo;
        final EditText etNombre = binding.etNombre;
        final EditText etApellidos = binding.etApellido;
        final EditText etPiso = binding.etPiso;
        final EditText etTelefono = binding.etTelefono;

        model.getFotoPerfil().observe(getViewLifecycleOwner(), fotoUrl -> {
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                Picasso.get().load(fotoUrl).placeholder(R.drawable.user).into(ivPerfil);
            } else {
                ivPerfil.setImageResource(R.drawable.user);
            }
        });
        model.getCorreo().observe(getViewLifecycleOwner(), tvCorreo::setText);
        model.getNombre().observe(getViewLifecycleOwner(), etNombre::setText);
        model.getApellidos().observe(getViewLifecycleOwner(), etApellidos::setText);
        model.getPiso().observe(getViewLifecycleOwner(), etPiso::setText);
        model.getTelefono().observe(getViewLifecycleOwner(), etTelefono::setText);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}