package com.example.proyectoad.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoad.databinding.FragmentInicioBinding;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;

    private InicioViewModel model;
    private InicioRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(this).get(InicioViewModel.class);

        RecyclerView recyclerView = binding.rvIncidencias;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InicioRecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        model.getIncidenciasLiveData().observe(getViewLifecycleOwner(), incidencias -> {
            if(incidencias != null){
                adapter.setListaIncidencias(incidencias);
                adapter.setCargando(false);
            }
        });

        model.cargarIncidencias();
        adapter.setCargando(true);

    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
