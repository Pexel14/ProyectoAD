package com.example.proyectoad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoad.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "Login";

    private ActivityLoginBinding binding;

    private DatabaseReference ref;
    private boolean encontrado;

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());



                try {
                    GoogleSignInAccount googleSignInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "Inicio de sesion COMPLETADO " + mAuth.getCurrentUser().getDisplayName() + " - " + mAuth.getCurrentUser().getDisplayName());
                                guardarCorreo();
                                String usuario = mAuth.getCurrentUser().getEmail();
                                Log.d(TAG, "Usuario: " + usuario);
                                String id = usuario.split("@")[0].replace(".", "");
                                String name = mAuth.getCurrentUser().getDisplayName();
                                mandarUsuarioInicio(id);
                            } else {
                                Log.d(TAG, "Inicio de sesion FALLIDO: " + task.getException());
                                Toast.makeText(LoginActivity.this, "Inicio de sesion fallido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.d(TAG, "Resultado: " + result.getResultCode());
            }
        }
    });

    private void guardarCorreo() {
        if (mAuth.getCurrentUser() == null) {
            String emailCortado = mAuth.getCurrentUser().getEmail().split("@")[0];
            if (emailCortado != null) {
                if (emailCortado.contains(".")) {
                    emailCortado.replace(".", "");
                    usuarioExiste(emailCortado);
                    if(!encontrado){
                        String correo = mAuth.getCurrentUser().getEmail();
                        String nombre = mAuth.getCurrentUser().getDisplayName();
                        ref.child(emailCortado).setValue(new User(nombre, correo)).addOnCompleteListener(command -> {
                            Toast.makeText(this, "Bienvenido a Alerty", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        }
    }

    private void usuarioExiste(String emailCortado) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subNodos : snapshot.getChildren()) {
                    if(emailCortado.equals(subNodos.child("correo").getValue(String.class).split("@")[0].replace(".",""))){
                        encontrado = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            String usuario = mAuth.getCurrentUser().getEmail();
            if (usuario != null) {
                String id = usuario.split("@")[0].replace(".", "");
                Log.d(TAG, "ID: " + id);

                mandarUsuarioInicio(id);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ref = FirebaseDatabase.getInstance().getReference("usuarios");

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        binding.tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = String.valueOf(binding.etLoginEmail.getText());
                String password = String.valueOf(binding.etLoginPassword.getText());

                // Comprobar si los campos están vacíos
                if (!email.isEmpty()) {
                    if (!password.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "signInWithEmail:success");
                                            String id = email.split("@")[0].replace(".", "");
                                            mandarUsuarioInicio(id);
                                        } else {
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            //TODO Mejorar mostrar fallos al usuario en Login
                                            Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Introduzca la contraseña", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Introduzca el correo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void mandarUsuarioInicio(String idUsuario) {

        SharedPreferences sharedPreferences = getSharedPreferences("MiAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", idUsuario);
        editor.apply();

        Intent m = new Intent(this, MainActivity.class);
        Intent f = new Intent(this, FormUserActivity.class);
        f.putExtra("id", idUsuario);

        ref = FirebaseDatabase.getInstance().getReference("usuarios");

        ref.child(idUsuario).addValueEventListener(new ValueEventListener() {

                    boolean hasDtaForm;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        hasDtaForm = snapshot.hasChild("telefono");

                        if (!hasDtaForm) {

                            // Como el usario entra directo no se crea ningun registro en la base de datos de firebase
                            // entoces aqui es necesario crear un registro con los datos del usuario por lo menos con
                            // el email como nunca pasa por el registro este tipo de usarios no tienen contraseña de nuestra app.

                            guardarUsuarioConGoogle(idUsuario);

                            startActivity(f);
                            finish();
                        } else {
                            startActivity(m);
                            finish();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void guardarUsuarioConGoogle(String id) {

        User user = new User("googleBody", id);

        // Guardar en Firebase Realtime Database
        ref.child(id).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Usuario guradado en la base de datos", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(LoginActivity.this, "Error inesperado al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}