package edu.ptit.saleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.ptit.saleapp.fragment.FragmentHome;

public class LoginActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_REGISTER=10000;
    Button btLogin;
    EditText etEmail, etPass;
    TextView tvForgetPass, tvRegester;
    ImageView imgFacebook, imageView5;
    protected FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        mFirebaseAuth = FirebaseAuth.getInstance();

        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.img_animator);
        animator.setTarget(imageView5);
        animator.start();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signInWithEmailAndPassword(
                                etEmail.getText().toString(), etPass.getText().toString()).
                        addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.toastSuccess), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                intent.putExtra("email", etEmail.getText().toString());
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,
                                        getResources().getString(R.string.toastError), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEmail.getText().toString().isEmpty())
                {
                    mFirebaseAuth.sendPasswordResetEmail(etEmail.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.toastForgotPass), Toast.LENGTH_LONG).show();
                                    } else {

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this,  getResources().getString(R.string.toastFillEmailForgot), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivityForResult(intent,REQUEST_CODE_REGISTER);
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/minhhieu.ta.5832"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REGISTER) {
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                final String email = data.getStringExtra("email");
                final String password = data.getStringExtra("pass");
                //Set lại giá trị cho txtEmail and password
                etEmail.setText(email);
                etPass.setText(password);
            } else {
                Toast.makeText(this, getResources().getString(R.string.toastError), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        btLogin =findViewById(R.id.btLoginLog);
        etEmail =findViewById(R.id.etEmailReg);
        etPass =findViewById(R.id.etPassReg);
        tvForgetPass =findViewById(R.id.forgotPassword);
        tvRegester =findViewById(R.id.gotoRegister);
        imgFacebook = findViewById(R.id.facebookLogin);
        imageView5 = findViewById(R.id.imageView5);
    }
}