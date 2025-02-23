package com.example.proyectoad.ui.inicio;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoad.Incidencias;
import com.example.proyectoad.R;

import java.util.ArrayList;

public class InicioRecyclerAdapter extends RecyclerView.Adapter<InicioRecyclerAdapter.IncioViewHolder> {

    private ArrayList<Incidencias> listaIncidencias;
    private boolean cargando;
    public InicioRecyclerAdapter(ArrayList<Incidencias> listaIncidencias){
        this.listaIncidencias = listaIncidencias;
        this.cargando = false;
    }

    @NonNull
    @Override
    public IncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_incidencia_home, parent, false);
        return new IncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncioViewHolder holder, int position) {
        Incidencias incidencia = listaIncidencias.get(position);
        holder.cargandoBarra(cargando);

        holder.tvTitulo.setText(incidencia.getTitulo());
        if(incidencia.getFoto().equals(String.valueOf(R.drawable.img_incidencia))){
            holder.imagen.setImageResource(R.drawable.img_incidencia);
        } else {
            holder.imagen.setImageURI(Uri.parse(incidencia.getFoto()));
        }
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    public void setListaIncidencias(ArrayList<Incidencias> listaIncidencias){
        this.listaIncidencias = listaIncidencias;
        notifyDataSetChanged();
    }

    public void setCargando(boolean cargando){
        this.cargando = cargando;
        notifyDataSetChanged();
    }

    static class IncioViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        ImageView imagen;
        TextView tvTitulo;

        public IncioViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.rvProgressBar);
            imagen = itemView.findViewById(R.id.ivImagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
        }

        public void cargandoBarra(boolean cargando){
            if(cargando){
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }

    }
}
