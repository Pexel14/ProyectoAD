package com.example.proyectoad.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentInicioBinding;

import java.util.ArrayList;

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

        if(binding != null){
            RecyclerView recyclerView = binding.rvIncidencias;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new InicioRecyclerAdapter(new ArrayList<>(), (position, mode) -> {
                Incidencia incidencia = model.getIncidenciasLiveData().getValue().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(incidencia.getId()));
                bundle.putInt("tipo", mode);
                Navigation.findNavController(view).navigate(R.id.detail_Incidencia_Fragment, bundle);
            }, model);
            recyclerView.setAdapter(adapter);

            model.getIncidenciasLiveData().observe(getViewLifecycleOwner(), this::cargarIncidencias);

            model.cargarIncidencias();
        }
    }

    private void cargarIncidencias(ArrayList<Incidencia> incidencias) {
        adapter.setListaIncidencias(incidencias);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}