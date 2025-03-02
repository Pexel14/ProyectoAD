package com.example.proyectoad.ui.creacion;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoad.Incidencia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailModelView extends ViewModel {

    private final MutableLiveData<Incidencia> incidenciaLiveData = new MutableLiveData<>();
    private DatabaseReference incidenciaRef = FirebaseDatabase.getInstance().getReference("incidencias");
    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private static boolean isAdmin;

    public boolean isAdmin(String email){
        usuariosRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isAdmin = snapshot.child("es_admin").getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isAdmin;
    }

    public void cargarIncidencia(String id){
        incidenciaRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String titulo = snapshot.child("titulo").getValue(String.class);
                String descripcion = snapshot.child("descripcion").getValue(String.class);
                String estado = snapshot.child("estado").getValue(String.class);
                String comentario = snapshot.child("comentario").getValue(String.class);
                String foto = snapshot.child("foto").getValue(String.class);
                String usuario = snapshot.child("usuario").getValue(String.class);

                incidenciaLiveData.postValue(new Incidencia(Integer.parseInt(id), titulo, descripcion, comentario, foto, estado, usuario));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<Incidencia> getIncidenciaLiveData() {
        return incidenciaLiveData;
    }
}
