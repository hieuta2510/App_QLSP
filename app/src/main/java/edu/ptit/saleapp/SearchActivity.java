package edu.ptit.saleapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.saleapp.adapter.AdapterProduct;
import edu.ptit.saleapp.model.Product;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private Spinner spCategory;
    private Button btSearch;
    private RecyclerView recycleView;
    private List<Product> mList;
    private AdapterProduct adapter;
    private DatabaseReference myRef;
    private TextView tvTKPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(gridLayoutManager);
        myRef = FirebaseDatabase
                .getInstance("https://saleapp-1412e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("productlist");
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = searchView.getQuery().toString().toLowerCase().trim();
                if(productName.isEmpty())
                {
                    getAllProduct();
                }
                else
                {
                    getProductByName(productName);
                }
            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spCategory.getItemAtPosition(position).toString();
                if(!category.equals("Tất cả"))
                {
                    getByCategory(category);
                }
                else
                {
                    getAllProduct();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        recycleView = findViewById(R.id.recySearch);
        searchView=findViewById(R.id.search);
        btSearch = findViewById(R.id.btSearch);
        spCategory = findViewById(R.id.spCateSearch);
        tvTKPro = findViewById(R.id.tvTKPro);
        String[] arr = getResources().getStringArray(R.array.spCategory);
        String[] arr1 = new String[arr.length+1];
        arr1[0] = "Tất cả";
        for (int i = 1;i < arr1.length;i++)
        {
            arr1[i] = arr[i-1];
        }
        spCategory.setAdapter(new ArrayAdapter<String >(this, R.layout.itemcate, arr1));
    }

    private void getByCategory(String category)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product product = dataSnapshot1.getValue(Product.class);
                    if (product.getCategory().equals(category))
                        mList.add(product);
                }
                adapter = new AdapterProduct(SearchActivity.this, mList, " ");
                recycleView.setAdapter(adapter);
                tvTKPro.setText(getResources().getString(R.string.thongke) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(SearchActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllProduct()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product product = dataSnapshot1.getValue(Product.class);
                    mList.add(product);
                }
                adapter = new AdapterProduct(SearchActivity.this, mList, " ");
                recycleView.setAdapter(adapter);
                tvTKPro.setText(getResources().getString(R.string.thongke) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(SearchActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getProductByName(String category)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Product product = dataSnapshot1.getValue(Product.class);
                    if(product.getName().toLowerCase().contains(category)) {
                        mList.add(product);
                    }
                }
                adapter = new AdapterProduct(SearchActivity.this, mList, " ");
                recycleView.setAdapter(adapter);
                tvTKPro.setText(getResources().getString(R.string.thongke) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(SearchActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}