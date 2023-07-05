package edu.ptit.saleapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallBack extends ItemTouchHelper.SimpleCallback {
    private AdapterHistory adaperHis;

    public SwipeToDeleteCallBack(AdapterHistory adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        adaperHis = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adaperHis.deleteHis(position);
    }
}
