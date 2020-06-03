package com.app.ptjasamutumineralindonesia.sampler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.ptjasamutumineralindonesia.MainActivity;
import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

public class MainSampler extends AppCompatActivity {

    Button btnLogout;
    LoginManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sampler");
        setContentView(R.layout.activity_main_sampler);
        sharedPrefManager = new LoginManager(this);

        btnLogout = (Button)findViewById(R.id.btn_logout_sampler);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(LoginManager.STATUS, false);
                sharedPrefManager.saveSPString(LoginManager.ACCESS_TOKEN, "");
                sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "");
                startActivity(new Intent(MainSampler.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }
}