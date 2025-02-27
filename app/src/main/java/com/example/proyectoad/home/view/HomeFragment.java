//package com.example.proyectoad.home.view;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.example.proyectoad.databinding.FragmentInicioBinding;
//import com.example.proyectoad.model.Inciencia;
//import com.example.proyectoad.ui.inicio.InicioViewModel;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeFragment extends Fragment {
//
//    private FragmentInicioBinding binding;
//
//    private IncidenRecyclerAdapter adapter;
//
//    private List<Inciencia> incList;
//
//    private final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("incidencias");

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        InicioViewModel homeViewModel =
//                new ViewModelProvider(this).get(InicioViewModel.class);
//
//        binding = FragmentInicioBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        binding.rvIncidencias.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        incList = new ArrayList<>();
//
//        adapter = new IncidenRecyclerAdapter(incList);
//        binding.rvIncidencias.setAdapter(adapter);
//
//        dbref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                incList.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    incList.add(dataSnapshot.getValue(Inciencia.class));
//                }
//
//                // Notificamos al adaptador del cambio
//                binding.rvIncidencias.getAdapter().notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Error al cargar las incidencias", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        /*final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}