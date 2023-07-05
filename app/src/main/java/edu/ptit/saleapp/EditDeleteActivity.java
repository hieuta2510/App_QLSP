package edu.ptit.saleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.Act;
import edu.ptit.saleapp.model.User;

public class EditDeleteActivity extends AppCompatActivity {
    private final static int galleryPick = 1;
    private String downloadImgUrl, randomKey, saveCurDate, saveCurTime;;
    private EditText tvName, tvPrice, tvDes;
    private Button btConfirm, btDelete, btCancel;
    private ImageView img;
    private Spinner spCate;
    private int check;
    private Uri imageUri;
    private String key = "", username;
    private DatabaseReference myRef;
    private StorageReference imgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        spCate.setAdapter(new ArrayAdapter<String>(this,R.layout.itemcate,getResources().getStringArray(R.array.spCategory)));
        check = 0;
        key = getIntent().getStringExtra("key");
        username = getIntent().getStringExtra("username");
        imgRef = FirebaseStorage.getInstance("gs://saleapp-1412e.appspot.com").getReference().child("product img");
        myRef = FirebaseDatabase.getInstance("https://saleapp-1412e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().
                child("productlist").child(key);
        displayProduct();

        // thay anh product
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1;
                openGallery();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1)
                {
                    storeImg();
                }
                else {
                    editProduct2();
                }
                saveHistory();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getResources().getString(R.string.tb_delete_product) + " " + tvName.getText().toString());
                builder.setMessage(getResources().getString(R.string.mess_delete_product));
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton(getResources().getString(R.string.btCancel), null);
                builder.setPositiveButton(getResources().getString(R.string.btConfirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProduct();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDeleteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void deleteProduct() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditDeleteActivity.this,
                        getResources().getString(R.string.mess_delete_product_ok), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditDeleteActivity.this,
                        MainActivity.class);
                startActivity(intent);
                saveHistory2();
                finish();
            }
        });
    }

    //luu hanh dong sua
    private void saveHistory()
    {
        Act act = new Act();
        SQLiteHelper db = new SQLiteHelper(this);
        User user = db.getUserByEmail(username);
        act.setUsername(user.getEmail());
        act.setFullname(user.getFullname());
        act.setAct(getResources().getString(R.string.edit_product) + " " + tvName.getText().toString());
        db.insertDataHistory(act);
    }

    // luu hanh dong xoa
    private void saveHistory2()
    {
        Act act = new Act();
        SQLiteHelper db = new SQLiteHelper(this);
        User user = db.getUserByEmail(username);
        act.setUsername(user.getEmail());
        act.setFullname(user.getFullname());
        act.setAct(getResources().getString(R.string.delete_product) + " " + tvName.getText().toString());
        db.insertDataHistory(act);
    }

    private void storeImg(){
        Calendar c=Calendar.getInstance();
        SimpleDateFormat curDate=new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate=curDate.format(c.getTime());
        SimpleDateFormat curTime=new SimpleDateFormat("HH:mm:ss");
        saveCurTime=curTime.format(c.getTime());
        randomKey=saveCurDate+"-"+saveCurTime;
        StorageReference filePath=imgRef.child(
                imageUri.getLastPathSegment()+randomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(EditDeleteActivity.this,
                        getResources().getString(R.string.error)+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditDeleteActivity.this,
                        getResources().getString(R.string.tb_edit_product), Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImgUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl=task.getResult().toString();
                        Toast.makeText(EditDeleteActivity.this,
                                getResources().getString(R.string.tb_url), Toast.LENGTH_SHORT).show();
                        editProduct();
                    }
                });
            }
        });
    }

    private void editProduct() {
        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString();
        String category = spCate.getSelectedItem().toString();
        String desc = tvDes.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_name), Toast.LENGTH_SHORT).show();
        } else if (price.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_price), Toast.LENGTH_SHORT).show();
        } else if (desc.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_des), Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> product = new HashMap<>();
            product.put("key", key);
            product.put("name", name);
            product.put("price", price);
            product.put("category", category);
            product.put("desc", desc);
            product.put("image", downloadImgUrl);
            myRef.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditDeleteActivity.this,
                                "Sửa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDeleteActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void editProduct2() {
        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString();
        String category = spCate.getSelectedItem().toString();
        String desc = tvDes.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_name), Toast.LENGTH_SHORT).show();
        } else if (price.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_price), Toast.LENGTH_SHORT).show();
        } else if (desc.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.miss_des), Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> product = new HashMap<>();
            product.put("key", key);
            product.put("name", name);
            product.put("price", price);
            product.put("category", category);
            product.put("desc", desc);
            myRef.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditDeleteActivity.this,
                                getResources().getString(R.string.tb_edit_product), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDeleteActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
    private void displayProduct() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("name").getValue().toString();
                    String price=snapshot.child("price").getValue().toString();
                    String category=snapshot.child("category").getValue().toString();
                    String desc=snapshot.child("desc").getValue().toString();
                    String image=snapshot.child("image").getValue().toString();
                    tvName.setText(name);
                    tvPrice.setText(price);
                    int p = 0;
                    for(int i = 0; i < spCate.getCount();i++)
                    {
                        if (spCate.getItemAtPosition(i).equals(category))
                        {
                            p = i;
                            break;
                        }
                    }
                    spCate.setSelection(p);
                    tvDes.setText(desc);
                    Picasso.get().load(image).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            img.setImageURI(imageUri);
        }
    }

    private void initView() {
        tvName = findViewById(R.id.etNameEdit);
        tvPrice = findViewById(R.id.etPriceEdit);
        tvDes = findViewById(R.id.etDesEdit);
        spCate = findViewById(R.id.spCateEdit);
        img=findViewById(R.id.imgEdit);
        btConfirm = findViewById(R.id.btConfirmEdit);
        btDelete = findViewById(R.id.btDeleteEdit);
        btCancel =findViewById(R.id.btCancelEdit);
    }
}