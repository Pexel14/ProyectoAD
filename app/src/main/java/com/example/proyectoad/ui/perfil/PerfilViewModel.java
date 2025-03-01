package com.example.proyectoad.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilViewModel extends ViewModel {

    private DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
    private FirebaseUser userLoged;

    private MutableLiveData<String> nombre = new MutableLiveData<>();
    private MutableLiveData<String> apellidos = new MutableLiveData<>();
    private MutableLiveData<String> nombreCompleto = new MutableLiveData<>();
    private MutableLiveData<String> correo = new MutableLiveData<>();
    private MutableLiveData<String> piso = new MutableLiveData<>();
    private MutableLiveData<String> telefono = new MutableLiveData<>();
    private MutableLiveData<Boolean> esAdmin = new MutableLiveData<>();
    private MutableLiveData<String> rol = new MutableLiveData<>();
    private MutableLiveData<String> fotoPerfil = new MutableLiveData<>();


    public PerfilViewModel() {
        userLoged = FirebaseAuth.getInstance().getCurrentUser();
        if (userLoged != null) {
            correo.setValue(userLoged.getEmail());
            cargarDatosPerfil();
        }
    }

    private void cargarDatosPerfil() {
        String email = userLoged.getEmail();
        String userId = email.split("@")[0];

        usuariosRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String nombreUsuario = dataSnapshot.child("nombre").getValue(String.class);
                    if (nombreUsuario != null) {
                        nombre.setValue(nombreUsuario);
                    }

                    String apellidosUsuario = dataSnapshot.child("apellidos").getValue(String.class);
                    if (apellidosUsuario != null) {
                        apellidos.setValue(apellidosUsuario);
                        nombreCompleto.setValue(nombreUsuario + " " + apellidosUsuario);
                    }

                    String correoUsuario = dataSnapshot.child("correo").getValue(String.class);
                    if (correoUsuario != null) {
                        if (correoUsuario.contains("@")) {
                            correo.setValue(correoUsuario);
                        } else {
                            correo.setValue(correoUsuario+"@gmail.com");
                        }
                    }

                    String pisoUsuario = dataSnapshot.child("piso").getValue(String.class);
                    if (pisoUsuario != null) {
                        piso.setValue(pisoUsuario);
                    }

                    String telefonoUsuario = dataSnapshot.child("telefono").getValue(String.class);
                    if (telefonoUsuario != null) {
                        telefono.setValue(telefonoUsuario);
                    }

                    Boolean adminStatus = dataSnapshot.child("es_admin").getValue(Boolean.class);
                    if (adminStatus != null) {
                        esAdmin.setValue(adminStatus);
                        rol.setValue(adminStatus ? "Administrador" : "Vecino");
                    }

                    String fotoPerfilId = dataSnapshot.child("foto_perfil").getValue(String.class);

                    if (userLoged.getPhotoUrl() != null) {
                        fotoPerfil.setValue(userLoged.getPhotoUrl().toString());
                    }
                    else if (fotoPerfilId != null && !fotoPerfilId.isEmpty()) {
                        fotoPerfil.setValue(fotoPerfilId);
                    }
                    else {
                        fotoPerfil.setValue(String.valueOf(R.drawable.user));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public LiveData<String> getNombre() {
        return nombre;
    }

    public LiveData<String> getApellidos() {
        return apellidos;
    }

    public LiveData<String> getNombreCompleto() {
        return nombreCompleto;
    }

    public LiveData<String> getCorreo() {
        return correo;
    }

    public LiveData<String> getPiso() {
        return piso;
    }

    public LiveData<String> getTelefono() {
        return telefono;
    }

    public LiveData<String> getRol() {
        return rol;
    }

    public LiveData<String> getFotoPerfil() {
        return fotoPerfil;
    }

}