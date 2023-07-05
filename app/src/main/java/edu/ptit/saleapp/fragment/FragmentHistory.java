package edu.ptit.saleapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import edu.ptit.saleapp.AddActivity;
import edu.ptit.saleapp.MainActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.adapter.AdapterHistory;
import edu.ptit.saleapp.adapter.SwipeToDeleteCallBack;
import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.Act;
import edu.ptit.saleapp.model.Product;

public class FragmentHistory extends Fragment {

    private AdapterHistory adapterHistory;
    private List<Act> mList;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmenthistory,
                container, false);
        initView(view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        mList = new ArrayList<>();
        // set code to retrive data and replace layout
        SQLiteHelper db = new SQLiteHelper(getContext());
        mList = db.getAllHistory();
        adapterHistory = new AdapterHistory(getActivity(), mList);
        recyclerView.setAdapter(adapterHistory);
        ItemTouchHelper.Callback callback = new SwipeToDeleteCallBack(adapterHistory);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }

    private void initView(View view) {
        recyclerView=view.findViewById(R.id.recycleHis);
    }



    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}