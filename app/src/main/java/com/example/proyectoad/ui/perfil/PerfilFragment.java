package com.example.proyectoad.ui.perfil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoad.LoginActivity;
import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentPerfilBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private Uri imagen;
    private StorageReference storageReference;
    private DatabaseReference usuariosReference;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        usuariosReference = FirebaseDatabase.getInstance().getReference("usuarios");

        View root = binding.getRoot();

        final ImageView ivPerfil = binding.imgPerfilUser;
        final TextView tvNombreCompleto = binding.tvNombre;
        final TextView tvCorreo = binding.tvCorreo;
        final TextView tvPiso = binding.tvPiso;
        final TextView tvTelefono = binding.tvTelefono;
        final TextView tvRol = binding.tvEsAdmin;

        perfilViewModel.getFotoPerfil().observe(getViewLifecycleOwner(), fotoUrl -> {
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                Picasso.get().load(fotoUrl).placeholder(R.drawable.user).into(ivPerfil);
            } else {
                ivPerfil.setImageResource(R.drawable.user);
            }
        });
        perfilViewModel.getNombreCompleto().observe(getViewLifecycleOwner(), tvNombreCompleto::setText);
        perfilViewModel.getCorreo().observe(getViewLifecycleOwner(), tvCorreo::setText);
        perfilViewModel.getPiso().observe(getViewLifecycleOwner(), tvPiso::setText);
        perfilViewModel.getTelefono().observe(getViewLifecycleOwner(), tvTelefono::setText);
        perfilViewModel.getRol().observe(getViewLifecycleOwner(), tvRol::setText);

        googleSignInClient = GoogleSignIn.getClient(requireContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());

        binding.btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        return root;
    }

    private void cerrarSesion() {
        mAuth.signOut();

        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
            Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}