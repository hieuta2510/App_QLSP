package edu.ptit.saleapp;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

import androidx.appcompat.app.AppCompatActivity;
        import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.saleapp.adapter.AdapterProduct;
import edu.ptit.saleapp.adapter.AdapterViewFlip;

public class ExampleUniTest extends AppCompatActivity implements AdapterProduct.ProductItemListener {
    private ViewFlipper viewFlipper;
    private RecyclerView rcvMA;
    private NavigationView nvgtvMA;
    private ListView lvMA;
    private AdapterViewFlip adapterViewFlip;
    private AdapterProduct adapterProduct;
    private DrawerLayout drawerLayout;
    private RecyclerView rViewProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        actionViewFlip();
        initProduct();
    }

    private void actionViewFlip() {
        List <Integer> lst = new ArrayList<>();
        lst.add(R.drawable.iphone_series);
        lst.add(R.drawable.oppo);
        lst.add(R.drawable.robohut);
        lst.add(R.drawable.galaxy);
        lst.add(R.drawable.loa);
        adapterViewFlip = new AdapterViewFlip(this, lst);
        for(int i = 0; i < adapterViewFlip.getCount(); i++)
        {
            viewFlipper.addView(adapterViewFlip.getView(i,null,null));
        }

        viewFlipper.setFlipInterval(8000); // Chuyển đổi sau mỗi 2 giây
        viewFlipper.setAutoStart(true); // Tự động chạy
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
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


    void init()
    {
        viewFlipper = findViewById(R.id.vfMA);
        rcvMA = findViewById(R.id.rcvMA);
        nvgtvMA = findViewById(R.id.nvgtvMA);
        lvMA = findViewById(R.id.lvMA);
        drawerLayout = findViewById(R.id.dlMA);
        rViewProduct = findViewById(R.id.rcvMA);
    }

    private void initProduct() {
        adapterProduct = new AdapterProduct(this);
        adapterProduct.setProductItemListener(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rViewProduct.setLayoutManager(manager);
        rViewProduct.setAdapter(adapterProduct);
    }

    @Override
    public void onItemClick(View v, int p) {

    }
}