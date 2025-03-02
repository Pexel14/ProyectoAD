package com.example.proyectoad.ui.creacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentDetailIncidenciaBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Detail_Incidencia_Fragment extends Fragment {

    private FragmentDetailIncidenciaBinding binding;
    private FirebaseUser usuario;
    private String [] estados;
    private DatabaseReference incidenciasRef;
    private StorageReference storageReference;
    private String id;
    private Uri imagen;

    private final ActivityResultLauncher<Intent> galeryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("DetailIncidencia", "ENTRA AQUI 2");
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    Log.d("DetailIncidencia", "ENTRA AQUI 1");
                    if(selectedImage != null){
                        imagen = selectedImage;
                        Picasso.get()
                                .load(selectedImage)
                                .placeholder(R.drawable.baseline_image_24).fit()
                                .error(R.drawable.baseline_image_24)
                                .into(binding.ivIncidencia);
                        Log.d("DetailIncidencia", "IMAGEN URI: " + imagen);
                    } else {
                        Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se selecciono ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }
    );


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailIncidenciaBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Regresa la vista raíz de binding
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        incidenciasRef = FirebaseDatabase.getInstance().getReference("incidencias");

        estados = new String[]{
                getString(R.string.detail_incidencia_fragment_sin_revisar),
                getString(R.string.detail_incidencia_fragment_en_revision),
                getString(R.string.detail_incidencia_fragment_completado)
        };

        id = getArguments().getString("id");
        int tipo = getArguments().getInt("tipo");
        Log.d("DetailIncidencia", "ID INCIDENCIA: " + id);

        DetailModelView detailModelView = new ViewModelProvider(this).get(DetailModelView.class);

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        String email = usuario.getEmail().split("@")[0].replace(",","");
        boolean isAdmin = detailModelView.isAdmin(email);

        detailModelView.cargarIncidencia(id);

        switch(tipo){
            case 0:
                binding.etComentario.setEnabled(false);
                binding.etDescripcion.setEnabled(false);
                binding.btnModificar.setVisibility(View.INVISIBLE);
                break;
            case 1:
                binding.ivIncidencia.setEnabled(true);
                binding.etDescripcion.setEnabled(true);
                binding.btnModificar.setVisibility(View.VISIBLE);
                break;
        }
        binding.spnEstado.setEnabled(false);
        binding.etComentario.setEnabled(false);

        if(id != null) {
            detailModelView.getIncidenciaLiveData().observe(getViewLifecycleOwner(), incidencia -> {
                if (incidencia != null) {
                    binding.tvNameIncidencia.setText(incidencia.getTitulo());
                    binding.etDescripcion.setText(incidencia.getDescripcion());

                    Picasso.get()
                            .load(incidencia.getFoto())
                            .fit()
                            .error(R.drawable.logo_app)
                            .placeholder(R.drawable.img_incidencia)
                            .into(binding.ivIncidencia);
                    String comentarios = incidencia.getComentario();

                    binding.tvUsuario.setText(incidencia.getUsuario());

                    binding.spnEstado.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, estados));

                    boolean encontrado = false;
                    for (int i = 0; i < estados.length && !encontrado; i++) {
                        if (incidencia.getEstado().equals(estados[i])) {
                            binding.spnEstado.setSelection(i);
                            encontrado = true;
                        }
                    }

                    if (!comentarios.isEmpty()) {
                        binding.etComentario.setText(comentarios);
                    }

                    if(tipo == 1){
                        if (isAdmin) {
                            binding.etComentario.setEnabled(true);
                            binding.spnEstado.setEnabled(true);
                        } else {
                            binding.spnEstado.setEnabled(false);
                        }

                        binding.ivIncidencia.setOnClickListener(v -> {
                            seleccionarImagen();
                        });
                    }


                    binding.btnModificar.setOnClickListener(v -> {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setTitle(R.string.detail_incidencia_titulo_alert).setMessage(R.string.detail_incidencia_texto_alert);
                        alertDialog.setPositiveButton(R.string.inicio_adapter_confirmacion, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.viewOscuro.setVisibility(View.VISIBLE);
                                binding.diProgressBar.setVisibility(View.VISIBLE);
                                String comentario = binding.etComentario.getText().toString();
                                if(!comentario.isEmpty()){
                                    incidencia.setComentario(comentario);
                                }

                                String estado = binding.spnEstado.getSelectedItem().toString();
                                incidencia.setEstado(estado);

                                String descripcion = binding.etDescripcion.getText().toString();
                                if(!descripcion.isEmpty()){
                                    incidencia.setDescripcion(descripcion);
                                }

                                Log.d("DetailIncidencia", "ID: " + id);
                                Log.d("DetailIncidencia","INCIDENCIA: " + incidencia.getId() + " - " + incidencia.getUsuario());


                                cambiarImagen(incidencia);
                            }
                        });
                        alertDialog.setNegativeButton(R.string.inicio_adapter_denegacion, null);
                        AlertDialog crearAlerta = alertDialog.create();
                        crearAlerta.show();
                    });
                }
            });
        }

        binding.ibtnGoBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_home);
        });
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        galeryLauncher.launch(intent);
    }

    private void cambiarImagen(Incidencia incidencia) {
        if(imagen != null){
            storageReference = FirebaseStorage.getInstance().getReference("incidencias");
            StorageReference archivo = storageReference.child(incidencia.getTitulo() + "_" + incidencia.getId() + ".jpg");
            archivo.delete();
            archivo.putFile(imagen).addOnSuccessListener(taskSnapshot -> {
                archivo.getDownloadUrl().addOnSuccessListener(uri -> {
                    incidencia.setFoto(uri.toString());
                    actualizarIncidencia(incidencia);
                });
            });
        } else {
            actualizarIncidencia(incidencia);
        }
    }

    public void actualizarIncidencia(Incidencia incidencia){
        incidenciasRef.child(id).setValue(incidencia).addOnCompleteListener(task -> {
            binding.viewOscuro.setVisibility(View.GONE);
            binding.diProgressBar.setVisibility(View.GONE);

            if(task.isSuccessful()){
                Toast.makeText(getContext(), R.string.detail_incidencia_exito_modificar, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.detail_incidencia_error_modificar, Toast.LENGTH_SHORT).show();
            }
        });

    }

}

