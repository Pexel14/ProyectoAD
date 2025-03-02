package com.example.proyectoad.ui.inicio;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class InicioViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Incidencia>> incidenciasLiveData = new MutableLiveData<>();

    private DatabaseReference refUsuarios;
    private DatabaseReference refIncidencias;
    private StorageReference storageReference;
    private FirebaseUser usuario;
    private static int cont;
    private static String id1;

    public InicioViewModel(){

        refIncidencias = FirebaseDatabase.getInstance().getReference("incidencias");
        refUsuarios = FirebaseDatabase.getInstance().getReference("usuarios");
        usuario = FirebaseAuth.getInstance().getCurrentUser();
    }

    public LiveData<ArrayList<Incidencia>> getIncidenciasLiveData(){
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
         refIncidencias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Incidencia> listaIncidencias = new ArrayList<>();
                String titulo;
                String foto;
                int id;
                for (DataSnapshot data : snapshot.getChildren()) {
                    titulo = data.child("titulo").getValue(String.class);
                    foto = data.child("foto").getValue(String.class);
                    id = data.child("id").getValue(Integer.class);

                    listaIncidencias.add(new Incidencia(id, titulo, foto));

                }
//            incidenciasLiveData.setValue(listaIncidencias);
            incidenciasLiveData.postValue(listaIncidencias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void incidenciasPorUser(String id){
        refUsuarios.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("incidencias")){
                    String [] incidencias = snapshot.child("incidencias").getValue(String.class).split(",");
                    if(incidencias.length != 0){
                        buscarIncidencias(incidencias);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buscarIncidencias(String[] incidencias) {
        ArrayList<Incidencia> listaIncidencias = new ArrayList<>();
        cont = 0;
        for (int i = 0; i < incidencias.length; i++) {
            refIncidencias.child(incidencias[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("id").exists()){
                        String titulo;
                        String foto;
                        Integer id;
                        Log.d("FragmentsIncidencias", snapshot.getKey() + " - " + snapshot.getChildrenCount());
                        titulo = snapshot.child("titulo").getValue(String.class);
                        foto = snapshot.child("foto").getValue(String.class);
                        id =Integer.parseInt(snapshot.child("id").getValue().toString());
                        cont++;
                        listaIncidencias.add(new Incidencia(id, titulo, foto));

                        if(cont == incidencias.length){
                            incidenciasLiveData.postValue(listaIncidencias);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void eliminarIncidencia(String key) {
        String email = usuario.getEmail().split("@")[0].replace(",","");
        refUsuarios.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(!user.getIncidencias().isEmpty()){
                    String incidencias = "";
                    String [] aux = user.getIncidencias().split(",");
                    for (String s : aux) {
                        if (!s.equals(key)) {
                            incidencias += s + ",";
                        } else {
                            borrarIncidenciaStorage(s);
                        }
                    }
                    if(!incidencias.isEmpty()){
                        incidencias = incidencias.substring(0, incidencias.length() -1);
                    }

                    user.setIncidencias(incidencias);
                    refUsuarios.child(email).setValue(user);
                    refIncidencias.child(key).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void borrarIncidenciaStorage(String s) {
        storageReference = FirebaseStorage.getInstance().getReference();
        refIncidencias.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bucket = "incidencias/" + snapshot.child("titulo").getValue(String.class) + "_" + snapshot.child("id").getValue().toString() + ".jpg";
                storageReference.child(bucket);
                storageReference.delete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
