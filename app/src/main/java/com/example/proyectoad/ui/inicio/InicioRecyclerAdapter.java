package com.example.proyectoad.ui.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoad.Incidencia;
import com.example.proyectoad.R;
import com.example.proyectoad.databinding.FragmentIncidenciasBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InicioRecyclerAdapter extends RecyclerView.Adapter<InicioRecyclerAdapter.IncioViewHolder> {

    private ArrayList<Incidencia> listaIncidencias;
    private boolean cargando;

    public InicioRecyclerAdapter(ArrayList<Incidencia> listaIncidencias){
        this.listaIncidencias = listaIncidencias;
        this.cargando = false;
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
            holder.cargandoBarra(cargando);
            holder.binding.tvTitulo.setText(incidencia.getTitulo());

            Picasso.get()
                    .load(incidencia.getFoto())
                    .fit()
                    .error(R.drawable.img_incidencia)
                    .placeholder(R.drawable.img_incidencia)
                .into(holder.binding.ivImagen);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO entrar dentro de la incidencia
                }
            });
        }
        cargando = true;
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    public void setListaIncidencias(ArrayList<Incidencia> listaIncidencias){
        this.listaIncidencias = listaIncidencias;
        notifyDataSetChanged();
    }

    public void setCargando(boolean cargando){
        this.cargando = cargando;
        notifyDataSetChanged();
    }

    public void limpiarDatos(){
        listaIncidencias.clear();
        notifyDataSetChanged();
    }

    static class IncioViewHolder extends RecyclerView.ViewHolder{

        private final FragmentIncidenciasBinding binding;

        public IncioViewHolder(@NonNull FragmentIncidenciasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void cargandoBarra(boolean cargando){
            if(cargando){
                binding.rvProgressBar.setVisibility(View.VISIBLE);
            } else {
                binding.rvProgressBar.setVisibility(View.GONE);
            }
        }

    }
}
