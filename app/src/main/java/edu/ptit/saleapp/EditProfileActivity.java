package edu.ptit.saleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etBirth;
    private TextView etEmail;
    private Button btConfirm, btCancel;
    private ImageView imgEditPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        intitView();
        Intent intent = getIntent();
        String name = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");
        String dob = intent.getStringExtra("dob");
        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.img_animator);
        animator.setTarget(imgEditPro);
        animator.start();
        etName.setText(name);
        etEmail.setText(email);
        etBirth.setText(dob);
        onClick();

    }

    private void onClick() {
        etBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent data = new Intent();
                if (validateName(etName.getText().toString()))
                {
                    data.putExtra("fullname", etName.getText().toString());
                    data.putExtra("email", etEmail.getText().toString());
                    data.putExtra("dob", etBirth.getText().toString());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                else
                {
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.toastValidate), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void intitView() {
        etName = findViewById(R.id.etNameEditif);
        etEmail = findViewById(R.id.etEmailEditif);
        etBirth = findViewById(R.id.etDoBEditif);
        btConfirm = findViewById(R.id.btConfirmEditif);
        btCancel = findViewById(R.id.btCancelEditif);
        imgEditPro = findViewById(R.id.imgEditPro);
    }

    private boolean validateName(String name)
    {
        if(name.matches(".*[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))
        {
            return false;
        }
        return true;
    }
}