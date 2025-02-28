package com.example.proyectoad.ui.creacion;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailModelView extends ViewModel {

    private final MutableLiveData<Incidencia> incidencia = new MutableLiveData<>();
    private DatabaseReference incidenciaRef = FirebaseDatabase.getInstance().getReference("incidencias");
    private String id;

    public void cargarIncidencia(){
        incidenciaRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String titulo = snapshot.child("titulo").getValue(String.class);
                String descripcion = snapshot.child("descripcion").getValue(String.class);
                String estado = snapshot.child("estado").getValue(String.class);
                String comentario = snapshot.child("comentario").getValue(String.class);
                String foto = snapshot.child("foto").getValue(String.class);

                incidencia.postValue(new Incidencia(Integer.parseInt(id), titulo, descripcion, comentario, foto, estado));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setId(String id) {
        this.id = id;
    }

    public LiveData<Incidencia>getIncidencia(){
        return incidencia;
    }

}
