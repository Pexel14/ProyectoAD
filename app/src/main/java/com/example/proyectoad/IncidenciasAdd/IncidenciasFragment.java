package com.example.proyectoad.IncidenciasAdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.proyectoad.databinding.FragmentIncidenciasAddBinding;

public class IncidenciasFragment extends Fragment {

    private FragmentIncidenciasAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IncidenciaViewModel incidenciaViewModel =
                new ViewModelProvider(this).get(IncidenciaViewModel.class);

        binding = FragmentIncidenciasAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.tvAddIncidencia;
        incidenciaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}