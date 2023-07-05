package edu.ptit.saleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.Act;
import edu.ptit.saleapp.model.User;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "To";
    private EditText etName, etPrice, etDes;
    private Spinner spCate;
    private Button btConfirm, btCancel;
    private ImageView img;
    private final static int galleryPick=1;
    private Uri imageUri;
    private String saveCurDate,saveCurTime;
    private String downloadImgUrl,randomKey;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    private String name,desc, price, category,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        spCate.setAdapter(new ArrayAdapter<String>(this, R.layout.itemcate, getResources().getStringArray(R.array.spCategory)));
        imgRef= FirebaseStorage.getInstance("gs://saleapp-1412e.appspot.com").getReference().child("product img");
        myRef= FirebaseDatabase.getInstance("https://saleapp-1412e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("productlist");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void validateData(){
        name= etName.getText().toString();
        price = etPrice.getText().toString();
        desc= etDes.getText().toString();
        category = spCate.getSelectedItem().toString();
        if(imageUri==null){
            Toast.makeText(this, getResources().getString(R.string.miss_pic), Toast.LENGTH_SHORT).show();
        }else if(desc.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.miss_des), Toast.LENGTH_SHORT).show();
        }else if(price.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.miss_price), Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.miss_name), Toast.LENGTH_SHORT).show();
        }else{
            storeProduct();
        }
    }
    private void storeProduct(){
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
                Toast.makeText(AddActivity.this,
                        getResources().getString(R.string.error)+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddActivity.this,
                        getResources().getString(R.string.tb_add_product), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddActivity.this,
                                getResources().getString(R.string.tb_url), Toast.LENGTH_SHORT).show();
                        storeProductToFirebase();
                    }
                });
            }
        });
    }

    private void storeProductToFirebase(){
        HashMap<String,Object> product=new HashMap<>();
        product.put("key",TAG+randomKey);
        product.put("name",name);
        product.put("price", price);
        product.put("category", category);
        product.put("desc",desc);
        product.put("image",downloadImgUrl);
        myRef.child(TAG+randomKey).updateChildren(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(AddActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name",name);
                            intent.putExtra("Price",price);
                            intent.putExtra("Description", desc);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            Intent intent1=new Intent(AddActivity.this,
                                    MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(AddActivity.this, getResources().getString(R.string.tb_add_product), Toast.LENGTH_SHORT).show();
                            saveHistory();
                        }else{
                            Toast.makeText(AddActivity.this,
                                    getResources().getString(R.string.tb_add_product_no_ok), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveHistory()
    {
        Act act = new Act();
        SQLiteHelper db = new SQLiteHelper(this);
        User user = db.getUserByEmail(username);
        act.setUsername(user.getEmail());
        act.setFullname(user.getFullname());
        act.setAct(getResources().getString(R.string.add_product) + " " + name);
        db.insertDataHistory(act);
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
        etName = findViewById(R.id.etNameAdd);
        etPrice = findViewById(R.id.etPriceAdd);
        etDes = findViewById(R.id.etDesAdd);
        spCate = findViewById(R.id.spCate);

        btConfirm = findViewById(R.id.btnSaveTask);
        btCancel = findViewById(R.id.btnCancel);
        img=findViewById(R.id.img);
    }
}