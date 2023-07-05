package edu.ptit.saleapp.fragment;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.ptit.saleapp.EditProfileActivity;
import edu.ptit.saleapp.R;
import edu.ptit.saleapp.database.SQLiteHelper;
import edu.ptit.saleapp.model.User;

public class FragmentInfo extends Fragment {
    private final static int REQUEST_CODE_EDIT=2507;
    private TextView tvName, tvEmail, tvBirth;
    private Button btEdit, btCancel;
    private String email;
    private ImageView imgInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmentinfo,
                container, false);
        initView(view);
        Bundle bundle = getArguments();
        email = bundle.getString("email");
        SQLiteHelper db = new SQLiteHelper(getContext());
        User user = db.getUserByEmail(email);
        tvName.setText(user.getFullname());
        tvEmail.setText(user.getEmail());
        tvBirth.setText(user.getDob());

        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.img_animator);
        animator.setTarget(imgInfo);
        animator.start();

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("fullname", tvName.getText().toString());
                intent.putExtra("email", tvEmail.getText().toString());
                intent.putExtra("dob", tvBirth.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
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
    public void onActivityResult(int requestCode,
                                    int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_EDIT) {
            if(resultCode == Activity.RESULT_OK) {
                final String name = data.getStringExtra("fullname");
                final String email = data.getStringExtra("email");
                final String dob = data.getStringExtra("dob");
                tvName.setText(name);
                tvEmail.setText(email);
                tvBirth.setText(dob);
                SQLiteHelper db = new SQLiteHelper(getContext());
                User user = db.getUserByEmail(email);
                user.setFullname(name);
                user.setDob(dob);
                db.updateUser(user);
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(View view) {
        tvName = view.findViewById(R.id.tvNameif);
        tvEmail = view.findViewById(R.id.tvEmailif);
        tvBirth = view.findViewById(R.id.tvBirthif);
        btEdit = view.findViewById(R.id.btEditif);
        btCancel = view.findViewById(R.id.btCancelif);
        imgInfo = view.findViewById(R.id.imgInfo);
    }
}