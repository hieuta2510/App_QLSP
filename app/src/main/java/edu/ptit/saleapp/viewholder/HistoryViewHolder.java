package edu.ptit.saleapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import edu.ptit.saleapp.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder{
    public TextView tvUsername, tvFullName, tvAct;

    public HistoryViewHolder(@NonNull View view)
    {
        super(view);
        tvUsername = view.findViewById(R.id.tvUsernameHis);
        tvFullName = view.findViewById(R.id.tvFullnameHis);
        tvAct = view.findViewById(R.id.tvActHis);
    }
}