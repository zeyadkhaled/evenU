package com.bilkentazure.evenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bilkentazure.evenu.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ayşegül  Gökçe on 1.05.2018
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Toolbar mToolbar;
    private ProgressDialog mProgress;

    private Button btnSave;
    private RelativeLayout rlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rlt = findViewById(R.id.reg_rlt);
        btnSave = findViewById(R.id.set_btn_save);
        final EditText edtName = findViewById(R.id.set_edt_name);
        final EditText edtDep = findViewById(R.id.set_edt_dep);
        //final EditText edtPass = findViewById(R.id.set_edt_password);

        //changing name and department informations
        User currentUser = MainActivity.userModel;
        edtName.setText(currentUser.getName());
        edtDep.setText(currentUser.getDepartment());

        mProgress = new ProgressDialog(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSave.setEnabled(false);

                View view = SettingsActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

               // String password = edtPass.getText().toString();
                String department = edtDep.getText().toString();
                String name = edtName.getText().toString();

                // the infos filled changing process
                if(!name.isEmpty() && !department.isEmpty()){
                    mProgress.setTitle("Saving...");
                    mProgress.setMessage("Please wait!");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    db.collection("users").document(mAuth.getCurrentUser().getUid()).update("department", department, "name",name).addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							Toast.makeText(SettingsActivity.this,"Done!", Toast.LENGTH_LONG).show();
							mProgress.hide();
							btnSave.setEnabled(true);
						}
					});

                    //change infos (email, password, department, name)
                } else {
					mProgress.hide();
					btnSave.setEnabled(true);
				}



            }
        });


    }

}