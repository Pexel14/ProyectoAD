package com.example.proyectoad.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectoad.databinding.FragmentHomeBinding;
import com.example.proyectoad.model.Inciencia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private IncidenRecyclerAdapter adapter;

    private List<Inciencia> incList;

    private final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("incidencias");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rvIncidencias.setLayoutManager(new LinearLayoutManager(getContext()));

        incList = new ArrayList<>();

        adapter = new IncidenRecyclerAdapter(incList);
        binding.rvIncidencias.setAdapter(adapter);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    incList.add(dataSnapshot.getValue(Inciencia.class));
                }

                // Notificamos al adaptador del cambio
                binding.rvIncidencias.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar las incidencias", Toast.LENGTH_SHORT).show();
            }
        });

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}