package com.example.proyectoad.ui.inicio;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoad.Incidencias;
import com.example.proyectoad.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InicioViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Incidencias>> incidenciasLiveData = new MutableLiveData<>();

    private DatabaseReference refUsuarios;
    private DatabaseReference refIncidencias;
    private FirebaseUser usuario;

    public InicioViewModel(){
        refIncidencias = FirebaseDatabase.getInstance().getReference("incidencias");
        refUsuarios = FirebaseDatabase.getInstance().getReference("usuarios");
        usuario = FirebaseAuth.getInstance().getCurrentUser();
    }

    public LiveData<ArrayList<Incidencias>> getIncidenciasLiveData(){
        return incidenciasLiveData;
    }

    public void cargarIncidencias(){
        if(usuario != null){
            String id = usuario.getEmail().split("@")[0].replace(".","");
            refUsuarios.child(id).child("es_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean esAdmin = snapshot.getValue(Boolean.class);
                    if(esAdmin){
                        cargarTodasIncidencias();
                    } else {
                        incidenciasPorUser(id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void cargarTodasIncidencias(){
         refIncidencias.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Incidencias> listaIncidencias = new ArrayList<>();
                Incidencias incidencia;
                for (DataSnapshot data : snapshot.getChildren()) {
                    incidencia = data.getValue(Incidencias.class);
                    if(incidencia != null){
                        listaIncidencias.add(incidencia);
                    }
                }
                incidenciasLiveData.setValue(listaIncidencias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void incidenciasPorUser(String id){
        refIncidencias.orderByKey().equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Incidencias> listaIncidencias = new ArrayList<>();
                Incidencias incidencia;
                for(DataSnapshot data : snapshot.getChildren()){
                    incidencia = data.getValue(Incidencias.class);
                    if(incidencia != null){
                        listaIncidencias.add(incidencia);
                    }
                }
                //incidenciasLiveData.setValue(listaIncidencias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
