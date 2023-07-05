package edu.ptit.saleapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.saleapp.AddActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.adapter.AdapterProduct;
import edu.ptit.saleapp.adapter.AdapterViewFlip;
import edu.ptit.saleapp.model.Product;


public class FragmentHome extends Fragment{
    FloatingActionButton fabutton;
    private ViewFlipper viewFlipper;
    private String username;
    private AdapterViewFlip adapterViewFlip;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    public List<Product> mList;
    private AdapterProduct adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_main,
                container, false);
        initView(view);
        Bundle bundle = getArguments();
        username = bundle.getString("username");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        myRef = FirebaseDatabase
                .getInstance("https://saleapp-1412e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("productlist");
        actionViewFlip();

        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        AddActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        fabutton=view.findViewById(R.id.fabutton);
        recyclerView=view.findViewById(R.id.rcvMA);
        viewFlipper = view.findViewById(R.id.vfMA);
    }



    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product product = dataSnapshot1.getValue(Product.class);
                    mList.add(product);
                }
                adapter = new AdapterProduct(getActivity(), mList, username);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionViewFlip() {
        List <Integer> lst = new ArrayList<>();
        lst.add(R.drawable.iphone_series);
        lst.add(R.drawable.oppo);
        lst.add(R.drawable.robohut);
        lst.add(R.drawable.galaxy);
        lst.add(R.drawable.loa);
        adapterViewFlip = new AdapterViewFlip(getContext(), lst);
        for(int i = 0; i < adapterViewFlip.getCount(); i++)
        {
            viewFlipper.addView(adapterViewFlip.getView(i,null,null));
        }

        viewFlipper.setFlipInterval(3000); // Chuyển đổi sau mỗi 2 giây
        viewFlipper.setAutoStart(true); // Tự động chạy
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX()) {
                    // Kéo sang phải
                    viewFlipper.showNext();
                } else if (e1.getX() < e2.getX()) {
                    // Kéo sang trái
                    viewFlipper.showPrevious();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }
}
