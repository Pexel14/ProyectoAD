package com.example.proyectoad.ui.inicio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentIncidenciasBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InicioRecyclerAdapter extends RecyclerView.Adapter<InicioRecyclerAdapter.IncioViewHolder> {

    private ArrayList<Incidencia> listaIncidencias;
    private final OnItemClickListener listener;
    private InicioViewModel model;

    public InicioRecyclerAdapter(ArrayList<Incidencia> listaIncidencias,  OnItemClickListener listener, InicioViewModel model){
        this.listaIncidencias = listaIncidencias;
        this.listener = listener;
        this.model = model;
    }

    @NonNull
    @Override
    public IncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentIncidenciasBinding binding = FragmentIncidenciasBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new IncioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IncioViewHolder holder, int position) {
        Incidencia incidencia = listaIncidencias.get(position);
        //Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.binding.imgProfile);

        if(incidencia != null){
            holder.binding.tvTitulo.setText(incidencia.getTitulo());

            Picasso.get()
                    .load(incidencia.getFoto())
                    .fit()
                    .error(R.drawable.img_incidencia)
                    .placeholder(R.drawable.img_incidencia)
                    .into(holder.binding.ivImagen);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, 0);
            }
        });

        holder.binding.btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, String.format(v.getContext().getString(R.string.inicio_adapter_compartir_incidencia), incidencia.getDescripcion()));
                v.getContext().startActivity(Intent.createChooser(intent, "Compartir con: "));
            }
        });

        holder.binding.btnEditar.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(position, 1);
            }
        });

        holder.binding.btnBorrar.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle(R.string.inicio_adapter_titulo_alerta).setMessage(R.string.inicio_adapter_mensaje_alerta);
            alertDialog.setPositiveButton(R.string.inicio_adapter_confirmacion, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(position < listaIncidencias.size()){
                        model.eliminarIncidencia(String.valueOf(listaIncidencias.get(position).getId()));

                        listaIncidencias.remove(position);
                        notifyItemRemoved(position);

                        Toast.makeText(v.getContext(), R.string.inicio_adapter_exito_eliminar, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.setNegativeButton(R.string.inicio_adapter_denegacion, null);
            AlertDialog crearAlerta = alertDialog.create();
            crearAlerta.show();
        });

    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int mode);
    }

    public void setListaIncidencias(ArrayList<Incidencia> listaIncidencias){
        this.listaIncidencias = listaIncidencias;
        notifyDataSetChanged();
    }

    static class IncioViewHolder extends RecyclerView.ViewHolder{

        private final FragmentIncidenciasBinding binding;

        public IncioViewHolder(@NonNull FragmentIncidenciasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}