package com.app.ptjasamutumineralindonesia.login;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.ptjasamutumineralindonesia.MainActivity;
import com.app.ptjasamutumineralindonesia.NofFoundPage;
import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.forgot.ForgotPassword;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.role.ChoicesRoleActivity;
import com.app.ptjasamutumineralindonesia.role.RoleResult;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.app.ptjasamutumineralindonesia.surveyor.MainSurveyor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    LoginManager sharedPrefManager;
    private Retrofit retrofit;
    public EditText mUsername, mPassword;
    public CheckBox mRememberMe;
    Button btn_login;
    String username, password;
    boolean rememberMe;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        sharedPrefManager = new LoginManager(this);
        retrofit = ApiBase.getClient();
        mUsername = (EditText) findViewById(R.id.edit_txt_username);
        mPassword = (EditText) findViewById(R.id.edit_txt_password);
        mRememberMe = (CheckBox) findViewById(R.id.edit_box_rememberMe);

//        if (sharedPrefManager.getSPSudahLogin()) {
//            Log.d("testing", sharedPrefManager.getUserRoles());
//            switch(sharedPrefManager.getUserRoles().toLowerCase()){
//                case "sampler":
//                    startActivity(new Intent(LoginActivity.this, MainSampler.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                    break;
//                case "surveyor":
//                    startActivity(new Intent(LoginActivity.this, MainSurveyor.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                    break;
//                default:
//                    startActivity(new Intent(LoginActivity.this, MainSurveyor.class)
//                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                    break;
//            }
//            finish();
//        }

        if (sharedPrefManager.getSPSudahLogin()) {
            startActivity(new Intent(LoginActivity.this, MainSampler.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("testing", "apa aja");
                final ApiLoginInterface service = retrofit.create(ApiLoginInterface .class);
                username =   mUsername.getText().toString();
                password =  mPassword.getText().toString();
                rememberMe = mRememberMe.isChecked();

                // param login
                JsonObject paramLogin = new JsonObject();
                try {
                    JSONObject jsonObj_ = new JSONObject();
                    jsonObj_.put("username", username);
                    jsonObj_.put("password", password);
                    jsonObj_.put("rememberMe", rememberMe);


                    JsonParser jsonParser = new JsonParser();
                    paramLogin = (JsonObject) jsonParser.parse(jsonObj_.toString());

                    //print parameter
                    Log.d("parameter for login  ", "AS PARAMETER  " + paramLogin);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<LoginResult> call=service.getStringScalar(paramLogin);
                Log.d("request login", call.request().toString());
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        Log.d("ini loh", response.raw().toString());
                        if(!response.isSuccessful()){
                            Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                        }else {
                            //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                            String msg = response.body().getMessage();
                            accessToken = response.body().getIdToken();
                            Log.d("ini token", accessToken);
                            sharedPrefManager.saveSPBoolean(LoginManager.STATUS, true);
                            sharedPrefManager.saveSPString(LoginManager.ACCESS_TOKEN, accessToken);
//                            Call<MeResult> getRole = service.getMeData("Bearer " + accessToken);
//                            Log.d("request getMeData ", getRole.request().headers().toString());
//                            getRole.enqueue(new Callback<MeResult>() {
//                                @Override
//                                public void onResponse(Call<MeResult> call, Response<MeResult> response) {
//                                    Log.d("response medata ", response.body().toString());
//                                    List<String> listRole = response.body().getAuthorities();
//
//                                    if(listRole.contains("ROLE_ADMIN")){
//                                        sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "ADMIN");
//                                        startActivity(new Intent(LoginActivity.this, MainSampler.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    } else if (listRole.contains("ROLE_SURVEYOR") && listRole.contains("ROLE_SAMPLER")){
//                                        sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "ADMIN");
//                                        startActivity(new Intent(LoginActivity.this, MainSampler.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    } else if (listRole.contains("ROLE_SURVEYOR")){
//                                        sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "SURVEYOR");
//                                        startActivity(new Intent(LoginActivity.this, MainSampler.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    } else if (listRole.contains("ROLE_SAMPLER")){
//                                        sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "SAMPLER");
//                                        startActivity(new Intent(LoginActivity.this, MainSampler.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    } else {
//                                        startActivity(new Intent(LoginActivity.this, NofFoundPage.class)
//                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MeResult> call, Throwable t) {
//                                    //for getting error in network put here Toast, so get the error on network
//                                    Log.d("Error DataPersonal ", t.getLocalizedMessage());
//                                    Toast.makeText(getBaseContext(),"Failed to getData personal, please try at a moment ",Toast.LENGTH_SHORT).show();
//                                }
//                            });
                            startActivity(new Intent(LoginActivity.this, MainSampler.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(getBaseContext(),"Failed to login, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void perform_forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

}
