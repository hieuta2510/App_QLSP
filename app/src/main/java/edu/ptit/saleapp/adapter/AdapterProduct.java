package edu.ptit.saleapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ptit.saleapp.EditDeleteActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.model.Product;
import edu.ptit.saleapp.viewholder.ProductViewHolder;

public class AdapterProduct extends RecyclerView.Adapter<ProductViewHolder> {
    private Context mContext;
    private List<Product> mList;
    private String username;

    public AdapterProduct(Context mContext,List<Product> mList, String username) {
        this.mContext = mContext;
        this.mList=mList;
        this.username=username;
    }

    public void setmList(List<Product> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product model=mList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvPrice.setText(model.getPrice().toString());
        holder.tvDes.setText(model.getDesc());
        Picasso.get().load(model.getImage()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, EditDeleteActivity.class);
                intent.putExtra("key",model.getKey());
                intent.putExtra("username", username);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}