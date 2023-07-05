package edu.ptit.saleapp.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ptit.saleapp.MainActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.User;

public class FragmentChangePassword extends Fragment {
    private ImageView imgChangePass;
    private EditText etOldPass, etNewPass, etNewPass2;
    private Button btConfirm, btCancel;
    private String password = "", newPass, newPass2;
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmentchangepass,
                container, false);
        initView(view);
        db = new SQLiteHelper(getContext());
        Bundle bundle = getArguments();
        String email = bundle.getString("email");
        User user = db.getUserByEmail(email);
        password = user.getPassword();
        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.img_animator);
        animator.setTarget(imgChangePass);
        animator.start();
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = etOldPass.getText().toString();
                if(password.equals(oldPass))
                {
                    newPass = etNewPass.getText().toString();
                    newPass2 = etNewPass2.getText().toString();
                    if(validate(newPass, newPass2))
                    {
                        Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        User user2 = db.getUserByEmail(email);
                        user2.setPassword(newPass);
                        db.updateUser(user2);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updatePassword(newPass).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        } else {
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                        getActivity().onBackPressed();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(), "Nhập sai mật khẩu cũ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        etOldPass = view.findViewById(R.id.etOldPass);
        etNewPass = view.findViewById(R.id.etNewPass);
        etNewPass2 = view.findViewById(R.id.etNewPass2);
        btConfirm = view.findViewById(R.id.btConfirmPass);
        btCancel = view.findViewById(R.id.btCancelPass);
        imgChangePass = view.findViewById(R.id.imgChangePass);
    }

    private boolean validate(String s1, String s2)
    {
        if(s1.equals(s2))
            return true;
        return false;
    }

}