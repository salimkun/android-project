package com.app.ptjasamutumineralindonesia.forgot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.MainActivity;
import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.ApiLoginInterface;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.login.LoginResult;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotPassword extends AppCompatActivity {

    private Retrofit retrofit;
    public EditText mEmail;
    Button btn_reset;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);
        retrofit = ApiBase.getClient();
        mEmail = (EditText) findViewById(R.id.edit_txt_email);

        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("testing", "apa aja");
                ApiForgotPassInterface service = retrofit.create(ApiForgotPassInterface .class);
                email =   mEmail.getText().toString();

                RequestBody body =
                        RequestBody.create(MediaType.parse("text/plain"), email);

                Call<Void> call = service.sendResetPasscode(body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getBaseContext(),"Please check your inbox email",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(),"Email not valid",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getBaseContext(),"Failed to send in your email, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void sample(View view) {
    }
}