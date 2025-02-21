package com.example.proyectoad.home.view;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentDetailIncidenciaBinding;
import com.squareup.picasso.Picasso;


public class Detail_Incidencia_Fragment extends Fragment {

    private FragmentDetailIncidenciaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailIncidenciaBinding.inflate(inflater, container, false);

        Intent i = getActivity().getIntent();

        if (i != null) {

            binding.tvNameIncidencia.setText(i.getStringExtra("titulo"));
            binding.tvDescripcion.setText(i.getStringExtra("descripcion"));
            binding.tvestado.setText(i.getStringExtra("estado"));
            Picasso.get()
                    .load(i.getStringExtra("imageUrl"))
                    .placeholder(R.drawable.baseline_image_24)
                    .into(binding.ivIncidencia);
            binding.tvComentario.setText(i.getStringExtra("comentario"));


        }

        binding.ibtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot(); // Regresa la vista raíz de binding
    }



}
