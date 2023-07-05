package edu.ptit.saleapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ptit.saleapp.R;
import edu.ptit.saleapp.model.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvName, tvPrice, tvDes;
        public ImageView img;
        public ItemClickListener listener;
        public ProductViewHolder(@NonNull View view) {
            super(view);
            tvName =view.findViewById(R.id.tvNameit);
            tvPrice =view.findViewById(R.id.tvPriceit);
            tvDes =view.findViewById(R.id.tvDesit);
            img=view.findViewById(R.id.imgit);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition(),false);
        }
}