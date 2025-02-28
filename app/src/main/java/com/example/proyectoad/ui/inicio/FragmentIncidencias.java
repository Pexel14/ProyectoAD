package com.example.proyectoad.ui.inicio;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentIncidenciasBinding;
import com.example.proyectoad.databinding.FragmentInicioBinding;
import com.example.proyectoad.ui.creacion.DetailModelView;
import com.example.proyectoad.ui.creacion.Detail_Incidencia_Fragment;
import com.squareup.picasso.Picasso;

public class FragmentIncidencias extends Fragment {

    private FragmentIncidenciasBinding binding;

    public static FragmentIncidencias newInstance(String key, String titulo, String foto){
        FragmentIncidencias fragment = new FragmentIncidencias();

        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("foto", foto);
        args.putString("key", key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIncidenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String titulo = getArguments().getString("titulo");
        String foto = getArguments().getString("foto");
        String key = getArguments().getString("key");

        binding.ivImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailModelView detailModelView = new ViewModelProvider(requireActivity()).get(DetailModelView.class);
                detailModelView.setId(key);
                Navigation.findNavController(v).navigate(R.id.detail_Incidencia_Fragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}