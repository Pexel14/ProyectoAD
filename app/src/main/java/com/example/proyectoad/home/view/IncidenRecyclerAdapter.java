//package com.example.proyectoad.home.view;

//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.proyectoad.R;
//import com.example.proyectoad.databinding.FragmentItemIncidenciaHomeBinding;
//import com.example.proyectoad.model.Inciencia;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;

/**
 *
 * En esta clase:
 *La clase IncidenRecyclerAdapter adapta una lista de objetos Inciencia para un RecyclerView,
 * mostrando datos e imágenes. Al hacer clic en un item, abre una nueva actividad con los detalles
 * de la incidencia seleccionada.
 *
 */

//public class IncidenRecyclerAdapter extends RecyclerView.Adapter<IncidenRecyclerAdapter.ItemViewHolder> {
//
//    // Lista de usuarios
//    private final List<Inciencia> userList;
//
//    // Creamos un constructor con la lista de usuarios
//    public IncidenRecyclerAdapter(List<Inciencia> userList) {
//        this.userList = userList;
//    }

//    @NonNull
//    @Override // Crea una nueva vista de usuario y la devuelve como objeto ViewHolder. Un ViewHolder almacena las vistas de cada elemento.
//    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        FragmentItemIncidenciaHomeBinding binding = FragmentItemIncidenciaHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        return  new ItemViewHolder(binding);
//
//    }
//
//    // Asigna datos a cada item, dentro del UserViewHolder.
//    @Override
//    public void onBindViewHolder(@NonNull IncidenRecyclerAdapter.ItemViewHolder holder, int position) {
//
//        // Obtenemos el objeto User correspondiente a la posición actual
//        Inciencia inci = userList.get(position);
//
//        // AQUI se guardan los datos para el item del boton
//
//        // Cargar imagen a traves de piccaso
//        Picasso.get()
//                .load(inci.getImageUrl())
//                .placeholder(R.drawable.baseline_image_24)
//                .into(holder.binding.ivImagen);
//
//
//
//        // AQUI Se trasportan los datos para la vista detalle
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i  = new Intent(v.getContext(), Detail_Incidencia_Fragment.class);
//                i.putExtra("titulo", inci.getTitulo());
//                i.putExtra("descripcion", inci.getDescripcion());
//                i.putExtra("comentario", inci.getComentario());
//                i.putExtra("imageUrl", inci.getImageUrl());
//                i.putExtra("estado", inci.getEstado());
//                v.getContext().startActivity(i);
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    // AQUI esta la Clase interna que almacena las vista en cada item del recycler view
//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        private final FragmentItemIncidenciaHomeBinding binding;
//
//        // Constructor que binding como parámetro
//        public ItemViewHolder(FragmentItemIncidenciaHomeBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }

//}
