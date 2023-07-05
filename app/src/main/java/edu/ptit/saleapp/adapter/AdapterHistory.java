package edu.ptit.saleapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ptit.saleapp.EditDeleteActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.Act;
import edu.ptit.saleapp.viewholder.HistoryViewHolder;
import edu.ptit.saleapp.viewholder.ProductViewHolder;

public class AdapterHistory extends RecyclerView.Adapter<HistoryViewHolder> {
    private Context mContext;
    private List<Act> mList;

    public AdapterHistory(Context mContext,List<Act> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Act> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.itemhistory,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Act model=mList.get(position);
        holder.tvUsername.setText(model.getUsername());
        holder.tvFullName.setText(model.getFullname());
        holder.tvAct.setText(model.getAct());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void deleteHis(int pos)
    {
        SQLiteHelper db = new SQLiteHelper(mContext);
        Act act = mList.get(pos);
        db.deleteHistory(act);
        mList.remove(pos);
        notifyDataSetChanged();
    }
}